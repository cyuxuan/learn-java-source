package cn.cyuxuan.agent.util;

import cn.cyuxuan.agent.sourceobject.MethodDescription;
import com.alibaba.fastjson.JSON;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class GenerateIdUtil {
    public static final int MAX_NUM = 1024 * 32;
    public final static AtomicInteger index = new AtomicInteger(0);
    /**
     * key: hashcode
     * value: methodId
     */
    public final static Map<Integer, Integer> methodInfos = new ConcurrentHashMap<>();
    // 在分布式环境下使用ConcurrentHashMap缓存方法详情
    // private final static Map<Integer, MethodDescription> methodTagAttr = new ConcurrentHashMap<>();
    // 简单起见，这里使用AtomicReferenceArray缓存方法详情
    public final static AtomicReferenceArray<MethodDescription> methodTagArr = new AtomicReferenceArray<>(MAX_NUM);

    public static int generateMethodId(Integer hashcode, String clazzName, String sourceClassName, String methodName, List<String> parameterNameList, List<String> parameterTypeList, String returnType) {
        if (methodInfos.containsKey(hashcode)) {
            return methodInfos.get(hashcode);
        }

        MethodDescription methodDescription = new MethodDescription();
        methodDescription.setClassName(clazzName);
        methodDescription.setSourceClassName(sourceClassName);
        methodDescription.setMethodName(methodName);
        methodDescription.setParameterNameList(parameterNameList);
        methodDescription.setParameterTypeList(parameterTypeList);
        methodDescription.setReturnType(returnType);

        int methodId = index.getAndIncrement();
        if (methodId > MAX_NUM) {
            return -1;
        }
        methodTagArr.set(methodId, methodDescription);
        methodInfos.put(hashcode, methodId);
        return methodId;
    }
}
