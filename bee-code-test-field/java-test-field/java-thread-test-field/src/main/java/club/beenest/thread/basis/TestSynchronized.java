package club.beenest.thread.basis;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 测试synchronized关键字
 */
public class TestSynchronized {
    private static Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        // 线程A
        new Thread(() -> {
            try {
                // 加锁
                lock.lock();
                // 线程A执行
                for(int i = 0; i< 50;i++) {
                    System.out.println(Thread.currentThread().getName() + "线程："+i);
                }
            } catch (Exception e) {
                // 异常
                e.printStackTrace();
            } finally {
                // 解锁
                lock.unlock();
            }
        },"A").start();

        // 线程B
        new Thread(() -> {
            try {
                // 加锁
                lock.lock();
                // 线程A执行
                for(int i = 0; i< 50;i++) {
                    System.out.println(Thread.currentThread().getName() + "线程："+i);
                }
            } catch (Exception e) {
                // 异常
                e.printStackTrace();
            } finally {
                // 解锁
                lock.unlock();
            }
        },"B").start();
    }
}
