package cn.cyuxuan.util;

import cn.cyuxuan.entity.ExecuteInfo;
import com.sun.management.ThreadMXBean;
import org.springframework.util.StopWatch;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * 获取当前线程信息的一些方法类
 */
public class PerfThreadUtils {
    private final static String PREFIX = "方法 ";
    private final static String USETIME = " 耗时 : ";

    /**
     * 线程本地变量 使用Map
     * Map中的键值对
     * 1. 键exeStack, 值Stack类, 记录方法执行栈，以待在方法进入和退出时执行信息统计
     * 2. 键exeSeq, 值List, 记录方法执行顺序，方法栈执行结束后，输出数据的时候，进行顺序输出
     * 3. 键exeInfoMap, 值Map, 记录方法执行信息
     */
    private static ThreadLocal<Map<String, Object>> threadLocal = new ThreadLocal<Map<String, Object>>();

    /**
     * 记录方法执行栈对象的键名
     */
    private final static String METHOD_EXECUTE_STACK_NAME = "exeStack";

    /**
     * 记录方法执行顺序的键名
     */
    private final static String METHOD_EXECUTE_SEQUENCE_KEY_NAME = "exeSeq";

    /**
     * 记录方法执行信息的 键名
     */
    private final static String METHOD_EXECUTE_INFO_MAP_KEY_NAME = "exeInfoMap";

    /**
     * 方法执行前，记录方法执行数据
     * 1. 方法执行的开始时间
     * 2. 方法执行顺序
     * 3. 方法开始前的线程使用内存计数
     *
     * @param methodName 方法的package名称
     */
    public static void handleBeforeData(String methodName) {
        // 实例化一个执行信息对象
        ExecuteInfo executeInfo = new ExecuteInfo(methodName);
        Map<String, Object> localData = new HashMap<>();
        localData = threadLocal.get();
        // 如果本地当前线程 本地变量 是空的则重新设置
        if (localData == null) {
            // 初始化本地变量
            initThreadLocalData(localData);
        }
        // 记录线程信息
        recordBeforeExeInfo(executeInfo);
    }

    /**
     * 处理方法执行完成后的执行信息数据
     */
    public static void handleAfterData() {
        // 方法执行结束，取出stopWatch
        Stack<StopWatch> stack = (Stack<StopWatch>) threadLocal.get().get(METHOD_EXECUTE_STACK_NAME);
        StopWatch stopWatch = stack.pop();
        // 优先获取停止时间，减少时间误差
        stopWatch.stop();
        // 获取执行信息
        List<String> list = (List<String>) threadLocal.get().get(METHOD_EXECUTE_SEQUENCE_KEY_NAME);
        // 获取stopWatch中存储的uuid
        // uuid 对应本次的执行信息
        String uuid = stopWatch.getId();
        Map<String, ExecuteInfo> map = (Map<String, ExecuteInfo>) threadLocal.get().get(METHOD_EXECUTE_INFO_MAP_KEY_NAME);
        ExecuteInfo executeInfo = map.get(uuid);
        executeInfo.executeTime = stopWatch.getLastTaskTimeMillis();
        // 获取线程内存信息以及gc信息
        getThreadAllocatedBytes((allocatedBytes, gcNum) -> {
            executeInfo.afterByteNum = allocatedBytes;
            executeInfo.afterGcNum = gcNum;
        });
        // 如果栈中为空则，方法执行完毕，写出对应的方法执行集合
        if (stack.size() == 0) {
            printInfo(list, map);
        }
    }

    /**
     * 执行方法信息记录
     *
     * @param executeInfo 本次方法执行信息
     */
    public static void recordBeforeExeInfo(ExecuteInfo executeInfo) {
        // 取出 方法执行栈
        Stack<StopWatch> stack = (Stack<StopWatch>) threadLocal.get().get(METHOD_EXECUTE_STACK_NAME);
        // 取出 方法执行顺序
        List<String> list = (List<String>) threadLocal.get().get(METHOD_EXECUTE_SEQUENCE_KEY_NAME);
        // 取出 方法执行信息
        Map<String, ExecuteInfo> map = (Map<String, ExecuteInfo>) threadLocal.get().get(METHOD_EXECUTE_INFO_MAP_KEY_NAME);
        // 生成UUID
        String uuid = UUID.randomUUID().toString();
        // 使用uuid进行时间标记
        StopWatch stopWatch = new StopWatch(uuid);
        // 存储执行信息
        executeInfo.executeId = uuid;
        executeInfo.level = stack.size();
        // 输出信息入集合
        list.add(uuid);
        map.put(uuid, executeInfo);
        // 当前时间监控入栈, uuid赋值给 StopWatch 在出栈的时候获取uuid对应的执行信息集合中的数据
        stack.push(stopWatch);
        // 开始监控时间
        stopWatch.start();
        // 获取此线程当前函数执行前的字节数量,以及前函数执行前的 gc 次数
        // 通过 判断gc次数来判断当前内存是否具有参考价值
        getThreadAllocatedBytes((allocatedBytes, gcNums) -> {
            executeInfo.beforeByteNum = allocatedBytes;
            executeInfo.beforegGcNum = gcNums;
        });
    }


    /**
     * 执行线程信息初始化
     * 线程本地变量集合
     *
     * @param localData 本地变量
     */
    public static void initThreadLocalData(Map<String, Object> localData) {
        localData = new HashMap<>();
        // 方法执行栈
        Stack<StopWatch> stack = new Stack<>();
        // 方法输出集合
        List<String> list = new ArrayList<>();
        // 方法输出集合
        Map<String, ExecuteInfo> map = new HashMap<>();
        localData.put(METHOD_EXECUTE_STACK_NAME, stack);
        localData.put(METHOD_EXECUTE_SEQUENCE_KEY_NAME, list);
        localData.put(METHOD_EXECUTE_INFO_MAP_KEY_NAME, map);
        threadLocal.set(localData);
    }


    /**
     * 获取内存信息
     *
     * @param executeInfo 函数执行信息
     */
    private static String getMemInfo(ExecuteInfo executeInfo) {
        long bytes = executeInfo.afterByteNum - executeInfo.beforeByteNum;
        long kbs = bytes / 1024;
        long ms = kbs / 1024;
        // 判断当前是否执行过gc
        boolean gcFlag = executeInfo.afterGcNum == executeInfo.beforegGcNum;
        String gcstr = gcFlag ? "" : "gc";
        return "memory(thread)(" + gcstr + "):[" + ms + "m]";
    }

    /**
     * 获取当前线程已经申请到的字节数
     *
     * @return 当前线程已经申请到的字节数
     */
    private static void getThreadAllocatedBytes(BiConsumer<Long, Long> function) {
        // 统计线程已经申请到的内存信息
        com.sun.management.ThreadMXBean threadMXBean
                = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        long threadId = Thread.currentThread().getId();
        long allocatedBytes = threadMXBean.getThreadAllocatedBytes(threadId);
        // 统计当前gc次数
        long gcNum = 0;
        for (final GarbageCollectorMXBean garbageCollector
                : ManagementFactory.getGarbageCollectorMXBeans()) {
            gcNum += garbageCollector.getCollectionCount();
        }
        // 赋值
        function.accept(allocatedBytes, gcNum);
    }

    /**
     * 输出执行信息
     *
     * @param list 方法执行顺序
     * @param map  方法执行键值对
     */
    private static void printInfo(List<String> list, Map<String, ExecuteInfo> map) {
        for (String infoId : list) {
            ExecuteInfo exeInfo = map.get(infoId);
            StringBuilder prefix = new StringBuilder();
            for (int i = 0; i < exeInfo.level; i++) {
                prefix.append("-- | ");
            }
            // 获取内存信息
            String memInfo = getMemInfo(exeInfo);
            prefix.append("--> ");
            String out = prefix + exeInfo.methodName + USETIME + exeInfo.executeTime
                    + "ms " + memInfo;
            System.out.println(out);
        }
    }
}
