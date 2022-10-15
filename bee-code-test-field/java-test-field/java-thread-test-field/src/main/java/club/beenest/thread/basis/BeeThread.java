package club.beenest.thread.basis;

/**
 * 通过继承Thread接口进行
 *
 * @author 陈玉轩
 * @since 2022/10/14
 */

public class BeeThread extends Thread {
    /**
     * 重写父类方法
     */
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            // 睡眠随机时间
            try {
                Thread.sleep(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("继承Thread类实现的线程" + i);
        }
    }
}
