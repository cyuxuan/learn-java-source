package club.beenest.thread.util;

public abstract class ThreadUtil {
    /**
     * 子线程标记前缀
     */
    public final static String SUB_THREAD_PREFIX = "线程-sub-";

    /**
     * 打印并返回线程信息
     *
     * @param message 想要打印的数据
     * @return 想要打印的数据
     */
    public static String printThreadMessage(String message) {
        String msg = SUB_THREAD_PREFIX + "[" + Thread.currentThread().getName() + "] : " + message;
        System.out.println(msg);
        return msg;
    }
}
