package club.beenest.thread.basis;

import static club.beenest.thread.util.ThreadUtil.SUB_THREAD_PREFIX;

/**
 * 测试线程停止
 *
 * @author 陈玉轩
 */

public class TestThreadStop {
    /**
     * 标记当前线程是否停止
     */
    private static volatile boolean isStop = false;

    public static void main(String[] args) throws InterruptedException {
        // 变量方式进行线程停止
//        testVariableFlag();
        // 通过interrupt变量停止
        testInterruptFlag();
    }

    /**
     * 测试线程停止，通过变量的方式
     * 这种方式不适用与线程睡眠的情况，睡眠的线程无法接收变量状态
     */
    public static void testVariableFlag() throws InterruptedException {
        // 开启第一个线程，并通过暂停标记执行停止
        new Thread(() -> {
            while (!isStop) {
                System.out.println(SUB_THREAD_PREFIX + Thread.currentThread().getName());
            }
        }, "1").start();
        new Thread(() -> {
            try {
                while (!isStop) {
                    // 线程进入睡眠状态时会停止在该处，因此无法进行变量值的判断
                    // 也就无法通过变量标记进行线程停止
                    Thread.sleep(10000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        // 停止三秒
        Thread.sleep(3000);
        // 改变标记
        isStop = true;
    }

    /**
     * 通过interrupt变量控制线程停止
     * interrupt()让sleep方法抛出异常，直接中断sleep()，我们可以通过捕获异常，然后继续执行后续代码
     */
    public static void testInterruptFlag() throws InterruptedException {
        // 开启第一个线程，并通过暂停标记执行停止
        Thread subThread_1 = new Thread() {
            @Override
            public void run() {
                // 判断是否终止线程了
                while (this.isInterrupted()) {
                    System.out.println(SUB_THREAD_PREFIX + Thread.currentThread().getName());
                }
            }
        };
        // 开启子线程一
        subThread_1.setName("1");
        subThread_1.start();

        // 开启第一个线程，并通过暂停标记执行停止
        Thread subThread_2 = new Thread() {
            @Override
            public void run() {
                // 判断是否终止线程了
                while (true) {
                    long startTime = System.currentTimeMillis();
                    try {
                        // 停止10秒
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        // 异常停止后会清楚中断标记，需要重新置标记
                        this.interrupt();
                        e.printStackTrace();
                    }
                    long endTime = System.currentTimeMillis();
                    long sumTime = endTime - startTime;
                    if (sumTime >= 10000) {
                        System.out.println(SUB_THREAD_PREFIX +
                                Thread.currentThread().getName() + "睡眠时间 外外外外外 停止了！！！");
                        break;
                    } else {
                        System.out.println(SUB_THREAD_PREFIX +
                                Thread.currentThread().getName() + "睡眠时间 内内内内内 停止了！！！");
                        break;
                    }
                }
            }
        };
        // 开启子线程一
        subThread_2.setName("2");
        subThread_2.start();
        // 主线程停止3秒
        Thread.sleep(3000);
        // 中断子线程一
        subThread_1.interrupt();
        // 中断子线程二
        subThread_2.interrupt();
    }
}
