package thread;

import java.util.concurrent.*;

/**
 * 线程池
 */
public class ExecutorDemo  {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建固定大小的线程池（其实底层用工厂模式创建不同类型的线程池）
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 提交Runnable任务
        executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("MyRunnable " + Thread.currentThread().getName() + " is runnning");
            }
        });

        // 提交Callable任务并获取返回值
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "MyCallable " + Thread.currentThread().getName() + " is runnning";
            }
        });
        System.out.println(future.get());

        executor.shutdown();
    }
}
