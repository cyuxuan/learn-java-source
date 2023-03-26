package cn.cyuxuan.agent;

import cn.cyuxuan.agent.transformer.DefaultTransformer;
import javassist.ClassClassPath;
import javassist.ClassPool;

import java.lang.instrument.Instrumentation;

public class AgentPreDemo {
    public static void premain(String agentArgs, Instrumentation inst) {
        // 获取 ClassPool 对象，使用系统默认类路径
//        ClassPool pool = ClassPool.getDefault();
        // 获取 ClassPool 对象，使用系统默认类路径
        ClassPool pool = new ClassPool(true);
//        String config = agentArgs;
        String config = "cn.cyuxuan";
        inst.addTransformer(new DefaultTransformer(config, pool));
    }
}
