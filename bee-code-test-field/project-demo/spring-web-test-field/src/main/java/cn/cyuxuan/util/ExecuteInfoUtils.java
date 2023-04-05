package cn.cyuxuan.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

public class ExecuteInfoUtils {
    public static String getExeMethodSignature(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 获取方法参数
        Object objs = joinPoint.getArgs();
        String[] inParams = methodSignature.getParameterNames();
        // 获取方法返回值
        String returnType = methodSignature.getReturnType().getTypeName();
        return "";
    }
}
