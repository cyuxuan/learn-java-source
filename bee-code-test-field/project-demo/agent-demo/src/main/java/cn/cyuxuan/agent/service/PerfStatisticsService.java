package cn.cyuxuan.agent.service;

import cn.cyuxuan.agent.result.Result;
import cn.cyuxuan.agent.result.ResultHandler;
import cn.cyuxuan.agent.sourceobject.MethodCommonDescription;
import cn.cyuxuan.agent.sourceobject.MethodRunTimeDescription;
import cn.cyuxuan.agent.util.MethodDescriptionUtil;
import com.sun.management.ThreadMXBean;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * 性能统计服务
 *
 * @author 陈玉轩
 * @since 23.04
 */
public class PerfStatisticsService {

    /**
     * 当前线程调用的方法压入当前线程对应的方法调用栈中
     *
     * @param methodId    通用方法描述id
     * @param inputValues 输入参数
     */
    public static void pushMethodCallInfoOnThread(int methodId, Object[] inputValues) {
        // 生成一个运行时信息
        MethodRunTimeDescription methodExeDescription = new MethodRunTimeDescription();
        // 获取当前运行时id
        String uuid = UUID.randomUUID().toString();
        // 设置当前运行时id
        methodExeDescription.setId(uuid);
        // 与通用描述信息关联起来
        methodExeDescription.setMethodDescriptionId(methodId);
        // 获取线程内存信息以及gc信息
        getThreadAllocatedBytes((allocatedBytes, gcNum) -> {
            methodExeDescription.setBeforeByteNum(allocatedBytes);
            methodExeDescription.setBeforegGcNum(gcNum);
        });
        // 结果入栈
        Result.resultPush(methodExeDescription);
        // 开启运行时间监控
        methodExeDescription.startWatch();
    }

    /**
     * 当前方法调用完成以后调用信息出栈
     */
    public static void popMethodCallInfoOnThread(Object returnValues) {
        MethodRunTimeDescription methodExeDescription = Result.resultPop();
        if (methodExeDescription == null) {
            // 执行结束
            return;
        }
        // 设置方法返回值
        methodExeDescription.setReturnValues(returnValues);
        // 停止监控
        methodExeDescription.stopWatch();
        // 获取当前线程的GC信息
        // 获取线程内存信息以及gc信息
        getThreadAllocatedBytes((allocatedBytes, gcNum) -> {
            methodExeDescription.setAfterByteNum(allocatedBytes);
            methodExeDescription.setAfterGcNum(gcNum);
        });
        // 如果当前栈没有数据，则开始执行数据打印
        if (Result.threadStackSize() == 0) {
            ResultHandler.printResult();
        }
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
}
