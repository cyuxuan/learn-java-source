package club.beenest.thread.normal.basis;

/**
 * 通过实现{@link java.lang.Runnable}接口实现多线程
 *
 * @author 陈玉轩
 * @date 2022/10/14
 * @since 1.0
 */

public class BeeThreadRunable implements Runnable{
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            // 睡眠随机时间
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("实现Runnable接口实现的线程" + i);
        }
    }
}
