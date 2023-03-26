package cn.cyuxuan.agent.util;

import cn.cyuxuan.agent.sourceobject.MethodDescription;
import com.alibaba.fastjson.JSON;

public class ResultHandler {
    public static void point(final int methodId, final long startNanos, Object[] parameterValues, Object returnValues) {
        MethodDescription method = GenerateIdUtil.methodTagArr.get(methodId);
        System.out.println("方法监控 - BEGIN");
        System.out.println("方法名称: " + method.getClassName() + "." + method.getMethodName());
        System.out.println("源方法名称: " + method.getSourceClassName() + "." + method.getMethodName());
        System.out.println("方法入参: " + JSON.toJSONString(method.getParameterNameList()) + "\n"
                + "入参类型:" + JSON.toJSONString(method.getParameterTypeList()) + "\n"
                + "入参值: " + JSON.toJSONString(parameterValues));
        System.out.println("方法出参: " + method.getReturnType() + "\n"
                + "出参值: " + JSON.toJSONString(returnValues));
        System.out.println("方法耗时: " + (System.nanoTime() - startNanos) / 1000_000 + "(ms)");
        System.out.println("方法监控 - END\r\n");
    }

    public static void point(final int methodId, Throwable throwable) {
        MethodDescription method = GenerateIdUtil.methodTagArr.get(methodId);
        System.out.println("方法监控 - BEGIN");
        System.out.println("方法名称: " + method.getClassName() + "." + method.getMethodName());
        System.out.println("方法异常: " + throwable.getMessage());
        System.out.println("方法监控 - END\r\n");
    }

}
