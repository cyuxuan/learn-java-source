package cn.cyuxuan.agent.transformer;

import cn.cyuxuan.agent.sourceobject.MethodDescription;
import cn.cyuxuan.agent.util.GenerateIdUtil;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * 参考 https://blog.csdn.net/wyg1973017714/article/details/122970626
 * 字节码文件转换
 */
public class DefaultTransformer implements ClassFileTransformer {
    /**
     * 配置哪些包下的类可以被加载
     */
    private final String config;
    private final ClassPool pool;

    public DefaultTransformer(String config, ClassPool pool) {
        this.config = config;
        // 设置pool的路径
//        ClassPool pool = ClassPool.getDefault();
//        ClassClassPath classPath = new ClassClassPath(this.getClass());
//        pool.insertClassPath(classPath);
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
        if (className == null
                || className.replaceAll("/", ".").startsWith("cn.cyuxuan.agent")
                || !className.replaceAll("/", ".").startsWith(this.config)) {
            return null;
        }
        try {
            System.out.println(className);
            className = className.replaceAll("/", ".");
            CtClass ctClass = pool.get(className);
            // 这里对类的所有方法进行监控,可以通过config配置具体监控哪些包下的类
            for (CtMethod method : ctClass.getDeclaredMethods()) {
                transformMethod(method, pool, className);
            }
            return ctClass.toBytecode();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 对传入类的方法进行改造
     *
     * @param method 方法
     * @param pool
     */
    private static void transformMethod(CtMethod method, ClassPool pool, String sourceClassName)
            throws NotFoundException, CannotCompileException {
        // 获取方法入参类型
        List<String> parameterTypeList = new ArrayList<>();
        for (CtClass parameterType : method.getParameterTypes()) {
            parameterTypeList.add(parameterType.getName());
        }

        // 获取方法入参名称
        List<String> parameterNameList = new ArrayList<>();
        CodeAttribute codeAttribute = method.getMethodInfo().getCodeAttribute();
        LocalVariableAttribute attribute
                = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        for (int i = 0; i < parameterTypeList.size(); i++) {
            parameterNameList.add(attribute.variableName(i));
        }

        // 获取ID标示，method会有一个唯一标识
        int idx = GenerateIdUtil.generateMethodId(method.hashCode(), method.getClass().getName(), sourceClassName,
                method.getName(), parameterNameList, parameterTypeList, method.getReturnType().getName());
        // 设置本地变量，开始时间
        method.addLocalVariable("startNanos", CtClass.longType);
        // 赋值时间变量
        method.insertBefore("startNanos = System.nanoTime();");
        // 设置本地变量，参数集合
        method.addLocalVariable("parameterValues", pool.get(Object[].class.getName()));
        // 赋值参数集合
        method.insertBefore("parameterValues = $args;");
        // 调用方法
        method.insertAfter("cn.cyuxuan.agent.util.ResultHandler.point(" + idx + ", startNanos, parameterValues, $_);", false);
        // 增加异常
        method.addCatch("cn.cyuxuan.agent.util.ResultHandler.point(" + idx + ", $e); throw $e;", pool.get("java.lang.Exception"));
    }
}
