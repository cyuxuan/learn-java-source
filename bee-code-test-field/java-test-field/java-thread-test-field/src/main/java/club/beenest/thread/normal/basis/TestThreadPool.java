package club.beenest.thread.normal.basis;

import club.beenest.thread.normal.util.ThreadUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 测试线程池的使用
 *
 * @author 陈玉轩
 */

public class TestThreadPool {
    public static void main(String[] args) {
        // 测试java的线程池api
        testJavaThreadPool();
    }

    /**
     * 测试java的线程池接口
     * {@link Executors#newSingleThreadExecutor} 该方式时每次单独生成一个线程
     * {@link Executors#newCachedThreadPool} 该方式没有核心线程，但是最大线程达数量达到最大限制
     * 来了任务就创建线程运行，当线程空闲超过60秒，就销毁线程，没有任何线程保留活跃
     * {@link Executors#newFixedThreadPool} N个核心线程
     */
    private static void testJavaThreadPool() {
//       ExecutorService threadPool =  Executors.newFixedThreadPool(5);
//       ExecutorService threadPool =  Executors.newSingleThreadExecutor();
        ExecutorService threadPool = Executors.newCachedThreadPool();

        //10个顾客请求
        try {
            for (int i = 1; i <= 10; i++) {
                Thread thread = new InTestThreadPool();
                thread.setName(String.valueOf(i));
                threadPool.execute(thread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    static class InTestThreadPool extends Thread {
        @Override
        public void run() {
            ThreadUtil.printThreadMessage("");
        }
    }
}
