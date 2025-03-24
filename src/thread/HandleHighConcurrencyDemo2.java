package thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ģ�⴦��߲���ʵ��
 * ��������ɱ���޿�棬���̴߳���
 * �Ľ���
 * 1.ʹ��juc����AtomicInteger��֤������ԭ���ԣ���С��������
 * 2.����cas������� synchronized�������߳̾�����
 */
public class HandleHighConcurrencyDemo2 {
    private static final AtomicInteger stock = new AtomicInteger(10);

    // �����̳߳أ�����5�����15�����д��ʱ��50�룬������������1000��
    private static final ExecutorService executor = new ThreadPoolExecutor(
            5, 15,
            50L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadPoolExecutor.AbortPolicy() //���ύ�߳�ִ���������̣߳�
    );

    /**
     * ��ɱ���
     *
     * @param userid �û�id
     */
    public static boolean secKillRequest(String userid) {
        int currentStock;
        int newStock;
        do {
            currentStock = stock.get();
            if (currentStock <= 0) return false;
            newStock = currentStock - 1;
        } while (!stock.compareAndSet(currentStock, newStock));

        System.out.println(userid + " �����ɹ���ʣ���� " + newStock); // ��¼����ʱ��׼ȷֵ
        return true;
    }


    /**
     * ģ��10���û�����
     *
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            final String userid = "User-" + i;
            executor.submit(() -> secKillRequest(userid));
        }
        executor.shutdown();
    }
}
