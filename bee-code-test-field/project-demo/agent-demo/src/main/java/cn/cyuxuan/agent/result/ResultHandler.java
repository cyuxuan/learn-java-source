package cn.cyuxuan.agent.result;

import cn.cyuxuan.agent.condition.ResultCondition;
import cn.cyuxuan.agent.config.AgentConfig;
import cn.cyuxuan.agent.sourceobject.MethodCommonDescription;
import cn.cyuxuan.agent.sourceobject.MethodRunTimeDescription;
import cn.cyuxuan.agent.util.MethodDescriptionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ResultHandler {
    private static Logger logger = Logger.getLogger("ResultHandler");
    private final static String USETIME = " 耗时:";

    public static void printResult() {
        ThreadLocal<Map<String, Object>> threadLocal = null;
        try {
            threadLocal = Result.getThreadLocal();
            // 取出 方法执行顺序
            List<String> list = (List<String>) threadLocal.get().get(Result.METHOD_EXECUTE_SEQUENCE_KEY_NAME);
            // 取出 方法执行信息
            Map<String, MethodRunTimeDescription> map = (Map<String, MethodRunTimeDescription>)
                    threadLocal.get().get(Result.METHOD_EXECUTE_INFO_MAP_KEY_NAME);
            // 检查当前信息是否在可输出集合中
            MethodRunTimeDescription methodRunTimeDescription = map.get(list.get(0));
            // 判断是否跳过结果输出
            boolean skip = ResultCondition.skipPrint(methodRunTimeDescription.getMethodDescriptionId());
            if (!skip) {
                printInfo(list, map);
            }
        } finally {
            // 打印结束后移除线程本地变量
            if (threadLocal != null) {
                threadLocal.remove();
            }
        }
    }

    public static void exception(int id, Exception e) throws Exception {
        throw new Exception("method [" + id + "] : handle error", e);
    }

    /**
     * 输出执行信息
     *
     * @param list 方法执行顺序
     * @param map  方法执行键值对
     */
    private static void printInfo(List<String> list, Map<String, MethodRunTimeDescription> map) {
        List<String> heads = new ArrayList<>();
        List<String> tails = new ArrayList<>();
        for (String infoId : list) {
            MethodRunTimeDescription exeInfo = map.get(infoId);
            // 获取方法通用描述信息
            MethodCommonDescription methodDescription
                    = MethodDescriptionUtil.getMethodDescription(exeInfo.getMethodDescriptionId());

            StringBuilder prefix = new StringBuilder();
            for (int i = 0; i < exeInfo.getLevel(); i++) {
                prefix.append("-- | ");
            }
            // 获取内存信息
            String memInfo = getMemInfo(exeInfo);
            prefix.append("--> ");
            String head = prefix + methodDescription.getMethodName();
            heads.add(head);
            String tail = USETIME + exeInfo.getExeTime()
                    + "ms" + "   " + memInfo + "   " + methodDescription.getSourceClassName();
            tails.add(tail);
        }
        // 统计最长的头
        int longest = 0;
        for (String head : heads) {
            if (head.length() > longest) {
                longest = head.length();
            }
        }
        // 执行输出，不满足长度的则补齐长度
        for (int i = 0; i < heads.size(); i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = heads.get(i).length(); j < longest; j++) {
                builder.append(" ");
            }
            builder.append(" **");
            logger.info(heads.get(i) + builder + tails.get(i));
        }
    }

    /**
     * 获取内存信息
     *
     * @param executeInfo 函数执行信息
     */
    private static String getMemInfo(MethodRunTimeDescription executeInfo) {
        long bytes = executeInfo.getAfterByteNum() - executeInfo.getBeforeByteNum();
        long kbs = bytes / 1024;
        long ms = kbs / 1024;
        // 判断当前是否执行过gc
        boolean gcFlag = executeInfo.getAfterGcNum() == executeInfo.getBeforegGcNum();
        String gcstr = gcFlag ? "-" : "gc";
        return "thread-memory(" + gcstr + "):[" + ms + "m, " + bytes + "b]";
    }
}
