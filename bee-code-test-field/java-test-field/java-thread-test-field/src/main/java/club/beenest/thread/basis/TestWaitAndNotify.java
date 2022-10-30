package club.beenest.thread.basis;

import club.beenest.thread.util.ThreadUtil;

/**
 * 测试wait语句和notify语句
 *
 * @author 陈玉轩
 */

public class TestWaitAndNotify {
    private static final Object obj = new Object();

    /**
     * 标记产品数量，数量为上限为1
     */
    private static volatile int context = 0;

    public static void main(String[] args) {
        TestWaitAndNotify testWaitAndNotify = new TestWaitAndNotify();
        testWaitAndNotify.testWaitAndNotify();
    }

    public void testWaitAndNotify() {
        // 开启生产者
        Producer producer = new Producer();
        producer.setName("1");
        producer.start();

        // 开启消费者
        Consumer consumer =  new Consumer();
        consumer.setName("2");
        consumer.start();
    }


    class Producer extends Thread {
        @Override
        public void run() {
            // 每次执行都需要先获取锁
            while(true) {
                // 取到锁
                synchronized (obj) {
                    // 沉睡10秒，等待消费者提前开启
//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                    if(context <= 0) {
                        // 进行生产
                        context++;
                        System.out.println(ThreadUtil.printThreadMessage("生产者生产完毕-当前产品数量: "+context));
                        // 唤醒消费
                        obj.notifyAll();
                    } else {
                        // 否则说明已经存在产品1,进入等待消费
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    class Consumer extends Thread {
        @Override
        public void run() {
            while (true) {
                // 每次执行都需要先获取锁
                synchronized (obj) {
                    if(context >= 1) {
                        // 存在产品，执行消费
                        context--;
                        System.out.println(ThreadUtil.printThreadMessage("消费者消费完毕-当前产品数量: "+context));
                        obj.notifyAll();
                    } else {
                        // 否则执行进入等待
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
