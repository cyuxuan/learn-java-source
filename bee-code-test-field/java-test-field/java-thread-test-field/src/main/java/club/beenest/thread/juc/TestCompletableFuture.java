package club.beenest.thread.juc;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TestCompletableFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 无返回值的类型
        noReturn();
        System.out.println("---------------------------------------");
        // 有返回值的类型
        haveReturn();
        System.out.println("---------------------------------------");
        moni();
    }

    /**
     * 条件执行
     *
     * @throws ExecutionException   e
     * @throws InterruptedException e
     */
    private static void moni() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + "-----come in");
            int result = ThreadLocalRandom.current().nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("------1秒后出结果：" + result);
            return result;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("计算完成，更新系统: " + v);
            }
        }).exceptionally(e -> {
            e.printStackTrace();
            System.out.println("异常情况：" + Arrays.toString(e.getStackTrace()) + "\t" + e.getMessage());
            return null;
        });

        System.out.println(Thread.currentThread().getName() + "线程先去忙其它的任务");

        System.out.println(completableFuture.get());

        TimeUnit.SECONDS.sleep(3);
    }


    /**
     * 测试 有返回值类型
     *
     * @throws ExecutionException   异常处理
     * @throws InterruptedException 异常处理
     */
    private static void haveReturn() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "havaReturn";
        });
        System.out.println(completableFuture.get());
    }

    /**
     * 测试没有返回值的调用类型
     */
    private static void noReturn() {
        // 无返回之的类型
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
