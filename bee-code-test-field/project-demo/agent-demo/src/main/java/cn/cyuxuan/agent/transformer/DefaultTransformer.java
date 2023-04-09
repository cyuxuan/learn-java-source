package cn.cyuxuan.agent.transformer;

import cn.cyuxuan.agent.condition.TransformCondition;
import cn.cyuxuan.agent.config.AgentConfig;
import cn.cyuxuan.agent.sourceobject.MethodCommonDescription;
import cn.cyuxuan.agent.util.MethodDescriptionUtil;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * 字节码文件转换
 *
 * @author 陈玉轩
 */
public class DefaultTransformer implements ClassFileTransformer {
    private final ClassPool pool;

    public DefaultTransformer(ClassPool pool) {
        this.pool = pool;
    }

    /**
     * 对源字节码文件进行改造
     *
     * @param loader              类加载器
     * @param className           类名称
     * @param classBeingRedefined 如果这是由重定义或重传触发的，则被重定义或重传的类；如果这是类加载，则为null
     * @param protectionDomain    正在定义或重新定义的类的保护域
     * @param classfileBuffer     类文件格式的输入字节缓冲区——不得修改
     * @return 重新转换后的字节码文件
     * @throws IllegalClassFormatException 字节码修改不合法
     */
    @Override
    public byte[] transform(ClassLoader loader, String className,
                            Class<?> classBeingRedefined, ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) throws IllegalClassFormatException {
        System.out.println(className);
        boolean skip = TransformCondition.skipTransformClass(className);
        if (className == null || skip) {
            return null;
        }
        try {
            System.out.println(className);
            className = className.replaceAll("/", ".");
            CtClass ctClass = pool.get(className);
            // 这里对类的所有方法进行监控, 可以通过config配置具体监控哪些包下的类
            for (CtMethod method : ctClass.getDeclaredMethods()) {
                // 判断method是否在sql集合中，如果是则需要
                // 如果当前类在sql的集合中则需要进行sql匹配，否则正常操作
                List<String> agentSqlMethods = AgentConfig.getAgentSqlMethods();
                String finalClassName = className;
                if (agentSqlMethods.parallelStream().anyMatch(item -> {
                    return item.startsWith(finalClassName);
                })) {
                    // 匹配是否为需要的sql执行方法
                    if (agentSqlMethods.parallelStream().anyMatch(item -> {
                        return item.equals(finalClassName + "#" + method.getName());
                    })) {
                        transformSourceCode(method, pool, className);
                    } else {
                        return null;
                    }
                } else {
                    transformSourceCode(method, pool, className);
                }
            }
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 开始转换字节码数据
     *
     * @param method          当前转换的方法
     * @param pool            类池
     * @param sourceClassName 原class文件名称
     */
    private static void transformSourceCode(CtMethod method, ClassPool pool, String sourceClassName)
            throws NotFoundException, CannotCompileException {
        // 获取方法入参类型
        List<String> parameterTypeList = getInputPrameterTypeList(method);

        // 获取方法入参名称
        List<String> parameterNameList = getInputParameterNameList(parameterTypeList, method);

        // 初始化当前方法信息
        MethodCommonDescription methodDescription =
                MethodDescriptionUtil.generateMethodId(method, sourceClassName, parameterNameList, parameterTypeList);

        // 执行class文件方法级别的转换
        transformMethod(method, methodDescription, pool);
    }

    /**
     * 对传入类的方法进行改造
     *
     * @param method 方法
     * @param pool   类池
     */
    private static void transformMethod(CtMethod method, MethodCommonDescription methodDescription, ClassPool pool)
            throws NotFoundException, CannotCompileException {
        // 方法执行前，获取
        method.insertBefore("cn.cyuxuan.agent.service.PerfStatisticsService.pushMethodCallInfoOnThread("
                + methodDescription.getId() + ", $args);");
        // 调用方法, 传入方法描述id, 方法入参，方法
        method.insertAfter("cn.cyuxuan.agent.service.PerfStatisticsService.popMethodCallInfoOnThread($_);"
                , false);
        // 增加异常
        method.addCatch("cn.cyuxuan.agent.result.ResultHandler.exception(" +
                methodDescription.getId() + ", $e); throw $e;", pool.get("java.lang.Exception"));
    }

    /**
     * 获取方法入参类型集合
     *
     * @return 获取方法入参类型集合
     */
    private static List<String> getInputPrameterTypeList(CtMethod method) throws NotFoundException {
        List<String> parameterTypeList = new ArrayList<>();
        CtClass[] parameterTypes = method.getParameterTypes();
        if (parameterTypes == null) {
            // 没有入参
            return parameterTypeList;
        }
        for (CtClass parameterType : parameterTypes) {
            parameterTypeList.add(parameterType.getName());
        }
        return parameterTypeList;
    }

    /**
     * 获取方法入参名称集合
     *
     * @return 方法入参集合
     */
    private static List<String> getInputParameterNameList(List<String> parameterTypeList, CtMethod method) {
        List<String> parameterNameList = new ArrayList<>();
        CodeAttribute codeAttribute = method.getMethodInfo().getCodeAttribute();
        if (codeAttribute == null) {
            // 没有参数集合
            return parameterNameList;
        }
        LocalVariableAttribute attribute
                = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        for (int i = 0; i < parameterTypeList.size(); i++) {
            parameterNameList.add(attribute.variableName(i));
        }
        return parameterNameList;
    }
}
