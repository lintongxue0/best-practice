package thread;

import java.util.concurrent.*;

/**
 * �̳߳�
 */
public class ExecutorDemo  {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // �����̶���С���̳߳أ���ʵ�ײ��ù���ģʽ������ͬ���͵��̳߳أ�
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // �ύRunnable����
        executor.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("MyRunnable " + Thread.currentThread().getName() + " is runnning");
            }
        });

        // �ύCallable���񲢻�ȡ����ֵ
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
