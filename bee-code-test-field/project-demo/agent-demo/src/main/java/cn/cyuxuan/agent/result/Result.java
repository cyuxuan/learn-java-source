package cn.cyuxuan.agent.result;

import cn.cyuxuan.agent.sourceobject.MethodRunTimeDescription;

import java.util.*;

/**
 * 结果集合，存储当前捕获到的所有数据
 *
 * 1. 当前线程调用时
 */
public class Result {
    /**
     * 本地线程变量
     */
    private static ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();

    /**
     * 记录方法执行栈对象的键名
     */
    public final static String METHOD_EXECUTE_STACK_NAME = "exeStack";

    /**
     * 记录方法执行顺序的键名
     */
    public final static String METHOD_EXECUTE_SEQUENCE_KEY_NAME = "exeSeq";

    /**
     * 记录方法执行信息的 键名
     */
    public final static String METHOD_EXECUTE_INFO_MAP_KEY_NAME = "exeInfoMap";


    public static void resultPush(MethodRunTimeDescription methodExeDescription) {
        // 获取当前变量
        Map<String, Object> localData;
        localData = threadLocal.get();
        // 判断当前线程是否已经初始化完成
        if(threadLocal.get() == null) {
            // 执行初始化
            initThreadLocalData(localData);
        }
        // 记录线程信息
        // 取出 方法执行栈
        Stack<String> stack = (Stack<String>) threadLocal.get().get(METHOD_EXECUTE_STACK_NAME);
        // 取出 方法执行顺序
        List<String> list = (List<String>) threadLocal.get().get(METHOD_EXECUTE_SEQUENCE_KEY_NAME);
        // 取出 方法执行信息
        Map<String, MethodRunTimeDescription> map = (Map<String, MethodRunTimeDescription>) threadLocal.get().get(METHOD_EXECUTE_INFO_MAP_KEY_NAME);
        // 记录执行顺序
        list.add(methodExeDescription.getId());
        // 记录执行信息
        map.put(methodExeDescription.getId(), methodExeDescription);
        // 方法入执行栈
        // 方法执行层级
        methodExeDescription.setLevel(stack.size());
        stack.push(methodExeDescription.getId());
    }


    public static MethodRunTimeDescription resultPop() {
        // 取出 方法执行栈
        Stack<String> stack = (Stack<String>) threadLocal.get().get(METHOD_EXECUTE_STACK_NAME);
        // 取出 方法执行顺序
        List<String> list = (List<String>) threadLocal.get().get(METHOD_EXECUTE_SEQUENCE_KEY_NAME);
        // 取出 方法执行信息
        Map<String, MethodRunTimeDescription> map = (Map<String, MethodRunTimeDescription>) threadLocal.get().get(METHOD_EXECUTE_INFO_MAP_KEY_NAME);
        // 获取当前执行id
        String uuid = stack.pop();
        // 获取对应的运行时方法执行信息
        return map.get(uuid);
    }

    /**
     * 执行线程信息初始化
     * 线程本地变量集合
     *
     * @param localData 本地变量
     */
    public static void initThreadLocalData(Map<String, Object> localData) {
        localData = new HashMap<>();
        // 方法执行栈
        Stack<String> stack = new Stack<>();
        // 方法输出集合
        List<String> list = new ArrayList<>();
        // 方法输出集合
        Map<String, MethodRunTimeDescription> map = new HashMap<>();
        localData.put(METHOD_EXECUTE_STACK_NAME, stack);
        localData.put(METHOD_EXECUTE_SEQUENCE_KEY_NAME, list);
        localData.put(METHOD_EXECUTE_INFO_MAP_KEY_NAME, map);
        threadLocal.set(localData);
    }

    public static ThreadLocal<Map<String, Object>> getThreadLocal() {
        return threadLocal;
    }

    public static int threadStackSize() {
        Stack<String> stack = (Stack<String>) threadLocal.get().get(METHOD_EXECUTE_STACK_NAME);
        return stack.size();
    }
}
