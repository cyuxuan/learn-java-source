package cn.cyuxuan.agent.util;

import cn.cyuxuan.agent.sourceobject.MethodCommonDescription;
import javassist.CtMethod;
import javassist.NotFoundException;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MethodDescriptionUtil {
    /**
     * 预估一个 MethodDescription 450 byte
     * 默认最大允许450M数据
     * 超过则不在存储
     */
    public static final int MAX_NUM = 1024 * 1024;

    /**
     * 存储所有的方法通用描述信息
     */
    private final static Map<Integer, MethodCommonDescription> methodInfoMap = new HashMap<>();

    /**
     * 可用的id集合
     */
    public static BitSet idSet = new BitSet(MAX_NUM);

    /**
     * 获取id时的锁
     */
    private static Lock lock = new ReentrantLock();

    /**
     * 生成方法的通用描述信息
     *
     * @param method            方法对象
     * @param sourceClassName   源文件名称
     * @param parameterNameList 参数名称集合
     * @param parameterTypeList 参数类型集合
     * @return 方法通用描述信息
     * @throws NotFoundException 异常处理
     */
    public static MethodCommonDescription generateMethodId(CtMethod method, String sourceClassName, List<String> parameterNameList,
                                                           List<String> parameterTypeList) throws NotFoundException {
        MethodCommonDescription methodDescription = new MethodCommonDescription();
        methodDescription.setClassName(method.getClass().getName());
        methodDescription.setSourceClassName(sourceClassName);
        methodDescription.setMethodName(method.getName());
        methodDescription.setParameterNameList(parameterNameList);
        methodDescription.setParameterTypeList(parameterTypeList);
        methodDescription.setReturnType(method.getReturnType().toString());
        // 设置唯一id
        int methodId = getId();
        methodDescription.setId(methodId);
        // 存储当前信息
        methodInfoMap.put(methodId, methodDescription);
        return methodDescription;
    }

    /**
     * 申请id
     *
     * @return 返回对应盛情到的id
     */
    public static int getId() {
        int id = -1;
        try {
            // 上锁
            lock.lock();
            for (int i = 1; i <= MAX_NUM; i++) {
                if (!idSet.get(i)) {
                    id = i;
                    // 标记已被使用
                    idSet.set(i, true);
                    break;
                }
            }
            return id;
        } finally {
            // 解锁
            lock.unlock();
        }
    }

    /**
     * 释放对应的id
     *
     * @param id 需要被释放的id
     */
    public static void freeId(int id) {
        idSet.set(id, false);
    }

    public static MethodCommonDescription getMethodDescription(int methodId) {
        return methodInfoMap.get(methodId);
    }
}
