package club.beenest.thread.normal.basis;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 测试Callable接口
 */
public class TestCallable {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        testCallable();
    }

    /**
     * 测试Callable接口
     * {@link Callable}
     * {@link FutureTask}
     */
    public static void testCallable() throws ExecutionException, InterruptedException {
        FutureTask<Integer> futureTask = new FutureTask<>(new Cthread());
        // 开启任务
        new Thread(futureTask).start();
        while (true) {
            if (futureTask.isDone()) {
                Integer res = futureTask.get();
                System.out.println("子线程结束：" + res);
                break;
            } else {
                System.out.println("子线程还没有结束：");
            }
        }

    }

    static class Cthread implements Callable<Integer> {
        @Override
        public Integer call() throws Exception {
            // 线程停止10秒，然后返回结果
            Thread.sleep(10000);
            System.out.println(2);
            return 2;
        }
    }
}
