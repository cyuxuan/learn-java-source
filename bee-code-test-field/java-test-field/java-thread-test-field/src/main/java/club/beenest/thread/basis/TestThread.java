package club.beenest.thread.basis;

import org.openjdk.jol.vm.VM;
import sun.misc.Unsafe;

import java.lang.Thread;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 测试{@link java.lang.Thread}
 */
public class TestThread {
    private static volatile Unsafe unsafe;
    private volatile static Boolean flag = false;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws InterruptedException {
        // 测试创建线程
//        testCreateThread();
        // 测试中断标记
//        testInterrupted();
        // 测试线程优先级
//        testThreadPriority();
        // 测试线程的礼让
//        testThreadComity();
        // 测试线程的加入
//        testThreadJoining();
        // 测试多线程情况下内存是否写回
        testThreadJmm();
    }

    /**
     * 测试创建线程
     * 主要是通过接口和继承类的方式
     */
    private static void testCreateThread() {
        // 实例化Thread类
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                // 睡眠随机时间
                try {
                    Thread.sleep(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("实例化Thread对象创建的" + i);
            }
        });
        thread.start();

        // 继承Thread接口
        BeeThread beeThread = new BeeThread();
        beeThread.start();

        // 实现Runnable接口
        BeeThreadRunable beeThreadRunable = new BeeThreadRunable();
        Thread tThread = new Thread(beeThreadRunable);
        tThread.start();
    }

    /**
     * 测试线程中断，使用中断标记进行优雅中断
     * 通过{@link java.lang.Thread}的interrupted参数进行判断
     *
     * @throws InterruptedException 中断异常
     */
    private static void testInterrupted() throws InterruptedException {
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                // 判断是否要求中断
                if (Thread.currentThread().isInterrupted()) {
                    // 如果是则执行终端
                    System.out.println("中断了");
                    // 中断
                    break;
                } else {
                    System.out.println("正常执行中" + i);
                }
            }
        });
        // 启动子线程
        thread.start();
        // 主线程睡眠3毫秒
        Thread.sleep(10);
        // 子线程执行中断标记
        thread.interrupt();
    }

    /**
     * 测试线程优先级
     * 通过{@link java.lang.Thread#setPriority(int)}设置优先级
     * 优先级越大获得cpu资源得概率越大，并不意味着一定会优先执行
     */
    private static void testThreadPriority() {
        // 用来存储当前线程执行的概率
        // TODO怎么判断当前线是否获得cpu资源
        List<String> list = new ArrayList<>();

        Thread t = new Thread(() -> {
            System.out.println("线程开始运行！");
        });

        t.setPriority(Thread.MIN_PRIORITY);  // 通过使用setPriority方法来设定优先级
        t.start();
    }

    /**
     * 测试线程的礼让
     * 使用{@link Thread#yield()} 进行礼让，但是如果礼让的线程优先级没有自己高，则会礼让失败
     * yield函数尝试放弃（注意：尝试）当前自身持有的资源，直接进入就绪状态，可继续参与争夺资源
     * wait函数会立即将自身持有的资源放弃，并进入等待队列，等待被唤醒
     */
    private static void testThreadComity() {
        Thread t1 = new Thread(() -> {
            System.out.println("线程1开始运行！");
            for (int i = 0; i < 50; i++) {
                if (i % 5 == 0) {
                    System.out.println("让位！");
                    Thread.yield();
                }
                System.out.println("1打印：" + i);
            }
            System.out.println("线程1结束！");
        });
        Thread t2 = new Thread(() -> {
            System.out.println("线程2开始运行！");
            for (int i = 0; i < 50; i++) {
                System.out.println("2打印：" + i);
            }
        });
        // 设置最高优先级
//        t1.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        // 设置最低优先级
//        t2.setPriority(Thread.MIN_PRIORITY);
        t2.start();
    }

    /**
     * 测试线程的加入
     * 在B线程中调用A线程的{@link java.lang.Thread#join()}方法，会在B线程中执行A线程至完成,被A.join的B线程会等待A线程执行完
     * A的执行依然与其它线程(C,D,E等，未执行A.join的线程)保持并发特性
     */
    private static void testThreadJoining() {
        Thread t1 = new Thread(() -> {
            System.out.println("线程1开始运行！");
            for (int i = 0; i < 50; i++) {
                System.out.println("1打印：" + i);
            }
            System.out.println("线程1结束！");
        });
        Thread t3 = new Thread(() -> {
            System.out.println("线程3开始运行！");
            for (int i = 0; i < 50; i++) {
                System.out.println("3打印：" + i);
            }
            System.out.println("线程3结束！");
        });
        Thread t2 = new Thread(() -> {
            System.out.println("线程2开始运行！");
            for (int i = 0; i < 50; i++) {
                System.out.println("2打印：" + i);
                if (i == 10) {
                    try {
                        System.out.println("线程1加入到此线程！");
                        t1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        t1.start();
        t2.start();
        t3.start();
    }

    /**
     * 分析java多线程情况下的内存模型以及对工作内存和主内存的处理
     *
     * @throws InterruptedException 中断异常信息
     */
    private static void testThreadJmm() throws InterruptedException {
        System.out.println("flag -> " + flag + " 主线程：对象地址：" + VM.current().addressOf(flag));
        Thread t1 = new Thread(() -> {
            System.out.println("线程一：监听flag::" + flag + " 线程一中的对象地址：" + VM.current().addressOf(flag));
            while (true) {
                if (flag) {
                    System.out.println("线程一：flag 变了 -> " + flag + " 线程一中的对象地址：" + VM.current().addressOf(flag));
                    break;
                }
//                else {
//                    System.out.println("flag 没变 -> " + flag);
//                }
            }
        });
        t1.start();

        Thread.sleep(2000);

        Thread t2 = new Thread(() -> {
            // 该线程用于修改flag
            flag = true;
            System.out.println("线程二：修改flag -> " + flag + " 线程二中的对象地址：" + VM.current().addressOf(flag));
        });
        t2.start();
        Thread.sleep(2000);
        System.out.println("主线程：中的flag -> " + flag + " 主线程中的对象地址：" + VM.current().addressOf(flag));
    }
}
