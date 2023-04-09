package cn.cyuxuan.agent.condition;

import cn.cyuxuan.agent.config.AgentConfig;

import java.util.List;

public class TransformCondition {
    public static boolean skipTransformClass(String className) {
        boolean skip;
        String finalClassName = className.replaceAll("/", ".");
        // 判断是否在排除集合中
        List<String> agentPackagesExcludes = AgentConfig.getAgentPackagesExcludes();
        skip = agentPackagesExcludes.parallelStream().anyMatch(finalClassName::startsWith);
        if (skip) {
            return true;
        }
        // 判断是否在指定的包集合中
        List<String> agentPackages = AgentConfig.getAgentPackages();
        skip = agentPackages.parallelStream().noneMatch(finalClassName::startsWith);
        if (skip) {
            return true;
        }
        // 判断是否在sql的包中
        if (AgentConfig.isAgentSqlEnable()) {
            // 判断包是否在sql中
            List<String> agentSqlMethods = AgentConfig.getAgentSqlMethods();
            skip = agentSqlMethods.parallelStream().anyMatch(item -> {
                return item.startsWith(className);
            });
        }
        return skip;
    }
}
