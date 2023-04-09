package cn.cyuxuan.agent.config;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AgentConfig {
    private static Logger logger = Logger.getLogger("AgentConfig");

    /**
     * 排除包的键
     */
    private static final String AGENT_PACKAGES_EXCLUDES_KEY = "agent.packages.excludes";

    /**
     * 排除包的键
     */
    private static final String AGENT_PACKAGES_KEY = "agent.packages";

    /**
     * 排除包的键
     */
    private static final String AGENT_ENTRANCE_KEY = "agent.entrance";

    /**
     * sql要拦截的包
     */
    private static final String AGENT_SQL_METHODS_KEY = "agent.sql.methods";


    private static Properties properties = new Properties();

    private static List<String> agentPackagesExcludes = new ArrayList<>();

    /**
     * 是否开启sql统计
     */
    private static boolean agentSqlEnable = true;

    /**
     * 当前探针需要打在哪些包的方法上面
     */
    private static List<String> agentPackages = new ArrayList<>();

    /**
     * 执行在上面 packages 范围内继续缩小探针范围
     * 从入口开始，入口涉及的方法才打探针，其余的不打探针
     */
    private static List<String> agentEntrances = new ArrayList<>();

    private static List<String> agentSqlMethods = new ArrayList<>();

    static {
        // 默认的排除包，防止探针循环打印
        agentPackagesExcludes.add("cn.cyuxuan.agent");
    }

    /**
     * 加载配置
     */
    public static void load() {
        // 获取配置文件
        File configFile =
                new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                        .getResource("statisticalAgent.properties")).getFile());

        // 获取文件输入流
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(configFile);
        } catch (Exception e) {
            logger.log(Level.WARNING, "get config file was error !!!");
        }

        try {
            // 解析文件
            properties.load(fileInputStream);
        } catch (Exception e) {
            logger.log(Level.WARNING, "config file parse error !!!");
        }

        // 解析配置文件
        parseConfig((String) properties.get(AGENT_PACKAGES_EXCLUDES_KEY), agentPackagesExcludes);
        parseConfig((String) properties.get(AGENT_PACKAGES_KEY), agentPackages);
        parseConfig((String) properties.get(AGENT_ENTRANCE_KEY), agentEntrances);
        parseConfig((String) properties.get(AGENT_SQL_METHODS_KEY), agentSqlMethods);
        // 解析sql配置
        parseSqlConfig((String) properties.get(AGENT_ENTRANCE_KEY));
    }


    private static void parseConfig(String itemSource, List<String> configList) {
        if (itemSource != null) {
            String[] items = itemSource.split(",");
            Collections.addAll(configList, items);
        }
    }

    private static void parseSqlConfig(String itemSource) {
        if ("false".equals(itemSource)) {
            agentSqlEnable = false;
        }
    }


    public static List<String> getAgentPackagesExcludes() {
        return agentPackagesExcludes;
    }

    public static List<String> getAgentPackages() {
        return agentPackages;
    }

    public static List<String> getAgentEntrances() {
        return agentEntrances;
    }

    public static boolean isAgentSqlEnable() {
        return agentSqlEnable;
    }

    public static List<String> getAgentSqlMethods() {
        return agentSqlMethods;
    }
}
