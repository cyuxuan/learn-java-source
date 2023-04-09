package cn.cyuxuan.agent;

import cn.cyuxuan.agent.config.AgentConfig;
import cn.cyuxuan.agent.transformer.DefaultTransformer;
import javassist.ClassPool;

import java.lang.instrument.Instrumentation;

public class AgentPreDemo {
    public static void premain(String agentArgs, Instrumentation inst) {
        // 加载配置文件
        AgentConfig.load();
        // 执行 class 文件按转换
        ClassPool pool = new ClassPool(true);
        inst.addTransformer(new DefaultTransformer(pool));
    }
}
