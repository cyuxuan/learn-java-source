package club.beenest.thread.normal.basis;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 测试线程池，拒绝策略
 */
public class TestThreadPoolRejected {
    public static void main(String[] args) {
        testRejected();
    }

    public  static void testCallerRunsPolicy() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,
                1,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 5; i++) {
            executor.execute(new InThreadPoolRejected("任务-" + i));
        }
        executor.shutdown();
    }

    /**
     * 测试自定义拒绝策略
     * {@link ThreadPoolExecutor.AbortPolicy}
     */
    public static void testRejected() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1,
                1,
                60L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(1),
                Executors.defaultThreadFactory(),
                (executedThread, executors) -> {
                    //自定义饱和策略
                    //记录一下无法处理的任务
                    System.out.println("无法处理的任务：" + executedThread.toString());
                });
        for (int i = 0; i < 5; i++) {
            executor.execute(new InThreadPoolRejected("任务-" + i));
        }
        executor.shutdown();
    }


    static class InThreadPoolRejected extends Thread {
        String name;
        public InThreadPoolRejected(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "处理" + this.name);
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        @Override
        public String toString() {
            return "Task{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}
