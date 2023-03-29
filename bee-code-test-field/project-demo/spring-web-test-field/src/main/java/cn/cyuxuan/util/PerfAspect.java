package cn.cyuxuan.util;

import com.sun.management.ThreadMXBean;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * 函数执行时间分析
 *
 * @author 陈玉轩
 * @since 23.03.26
 */
@Aspect
@Component
public class PerfAspect {
    private final static String PREFIX = "方法 ";
    private final static String USETIME = " 耗时 : ";

    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    @Pointcut(value = "execution(* cn.cyuxuan..*.*(..))")
    public void pointCut() {

    }


    /**
     * 前置通知
     *
     * @param joinPoint 切点
     */
    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String methodName = methodSignature.getMethod().getName();
        ExecuteInfo executeInfo = new ExecuteInfo();
        // 通过 method name 生成一组执行数据并记录下来
        this.handleBeforeData(methodName, executeInfo);
        // 获取此线程当前函数执行前的字节数量,以及前函数执行前的 gc 次数
        this.getThreadAllocatedBytes((allocatedBytes, gcNums) -> {
            executeInfo.beforeByteNum = allocatedBytes;
            executeInfo.beforegGcNum = gcNums;
        });
    }

    /**
     * 后置执行函数
     *
     * @param joinPoint
     */
    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint) {
        // 处理方法执行后的数据
        this.handleAfterData();
    }

    /**
     * 处理方法执行前的数据处理
     *
     * @param methodName 方法名称
     */
    private void handleBeforeData(String methodName, ExecuteInfo executeInfo) {
        Map<String, Object> localData = new HashMap<>();
        localData = threadLocal.get();
        // 执行栈
        if (localData == null) {
            localData = new HashMap<>();
            // 方法执行栈
            Stack<StopWatch> stack = new Stack<>();
            // 方法输出集合
            List<String> list = new ArrayList<>();
            // 方法输出集合
            Map<String, ExecuteInfo> map = new HashMap<>();
            localData.put("stack", stack);
            localData.put("list", list);
            localData.put("map", map);
            threadLocal.set(localData);
        }
        // 取出栈
        Stack<StopWatch> stack = (Stack<StopWatch>) threadLocal.get().get("stack");
        // 取出输出集合
        List<String> list = (List<String>) threadLocal.get().get("list");
        // 取出map
        Map<String, ExecuteInfo> map = (Map<String, ExecuteInfo>) threadLocal.get().get("map");
        // 生成UUID
        String uuid = UUID.randomUUID().toString();
        // 使用uuid进行时间标记
        StopWatch stopWatch = new StopWatch(uuid);
        // 存储执行信息
        executeInfo.executeId = uuid;
        executeInfo.methodName = methodName;
        executeInfo.level = stack.size();
        // 输出信息入集合
        list.add(uuid);
        map.put(uuid, executeInfo);
        // 当前时间监控入栈
        stack.push(stopWatch);
        // 开始监控时间
        stopWatch.start();
    }

    /**
     * 方法执行后的书处理
     */
    private void handleAfterData() {
        // 方法执行结束，取出stopWatch
        Stack<StopWatch> stack = (Stack<StopWatch>) threadLocal.get().get("stack");
        StopWatch stopWatch = stack.pop();
        stopWatch.stop(); // 优先停止时间
        // 获取执行信息
        List<String> list = (List<String>) threadLocal.get().get("list");
        // 刷新输出集合中的数据
        String uuid = stopWatch.getId();
        Map<String, ExecuteInfo> map = (Map<String, ExecuteInfo>) threadLocal.get().get("map");
        ExecuteInfo executeInfo = map.get(uuid);
        executeInfo.executeTime = stopWatch.getLastTaskTimeMillis();
        // 如果栈中为空则，方法执行完毕，写出对应的方法执行集合
        if (stack.size() == 0) {
            for (String infoId : list) {
                ExecuteInfo exeInfo = map.get(infoId);
                StringBuilder prefix = new StringBuilder();
                for (int i = 0; i < exeInfo.level; i++) {
                    prefix.append("-- | ");
                }
                prefix.append("--> ");
                String out = prefix + exeInfo.methodName + USETIME + exeInfo.executeTime;
                System.out.println(out);
            }
        }
    }

    /**
     * 获取当前线程已经申请到的字节数
     *
     * @return 当前线程已经申请到的字节数
     */
    private void getThreadAllocatedBytes(BiConsumer<Long, Long> function) {
        com.sun.management.ThreadMXBean threadMXBean
                = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        long threadId = Thread.currentThread().getId();
        long allocatedBytes = threadMXBean.getThreadAllocatedBytes(threadId);
        long gcNum = 0;
        for (final GarbageCollectorMXBean garbageCollector
                : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcNum += garbageCollector.getCollectionCount();
        }
        function.accept(allocatedBytes, gcNum);
    }


    /**
     * 执行信息
     */
    private class ExecuteInfo {
        /**
         * 当前这次执行的id, 唯一标记
         */
        public String executeId;

        /**
         * 当前执行的函数名称
         */
        public String methodName;

        /**
         * 当前执行的函数层级深度
         */
        public int level;

        /**
         * 当前函数执行的时间
         */
        public long executeTime;

        /**
         * 当前线程方法执行前的字节数量
         */
        public long beforeByteNum;

        /**
         * 当前线程方法执行后的字节数量
         */
        public long afterByteNum;

        /**
         * 方法执行前的gc次数
         */
        public long beforegGcNum;

        /**
         * 方法执行后的gc次数
         */
        public long afterGcNum;
    }
}
