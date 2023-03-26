package club.beenest.thread.juc;

import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * FutrueTask测试
 *
 * @author 陈玉轩
 * @since 12.22.05
 */
public class TestFutrueTask {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new MyThread());
        Thread t = new Thread(futureTask);
        t.start();
        // 获取结果 get 是会阻塞的
        String res = futureTask.get();
        System.out.println(res);
    }
}


class MyThread implements Callable<String> {
    @Override
    public String call() throws Exception {
        System.out.println("This is Callable!!!");
        return "callable";
    }
}
