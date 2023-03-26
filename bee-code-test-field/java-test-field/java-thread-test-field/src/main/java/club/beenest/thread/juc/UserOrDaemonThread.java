package club.beenest.thread.juc;

/**
 * 用户线程和守护线程
 *
 * @author 陈玉轩
 * @since 22.12.05
 */
public class UserOrDaemonThread {
    public static void main(String[] args) {
        // 判断当前线程是否为守护线程
        Thread.currentThread().isDaemon();
    }
}
