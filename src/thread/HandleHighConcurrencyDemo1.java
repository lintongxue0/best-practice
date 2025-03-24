package thread;

import java.util.concurrent.*;

/**
 * ģ�⴦��߲���ʵ��
 * ��������ɱ���޿�棬���̴߳���
 * ȱ�㣺
 *  1.volatileֻ�ܱ�֤�ɼ��ԣ����ܱ�֤�ñ�����ԭ���ԣ�
 *  2.synchronizedʹ���������������ȴ󣬲������ܽϵ�
 */
public class HandleHighConcurrencyDemo1 {
    // volatile ���ã�
    //   1. ��ֹCPUָ������
    //   2. �����ɼ��ԣ��߳��޸ĸñ������������µ����棨��Ϊ�߳�Ҳ���Լ��Ļ��棬һ���������渴��һ�ݵ��Լ��Ļ��������
    private volatile static int stock = 10;

    // �����̳߳أ�����10�����50�����д��ʱ��50�룬������������1000��
    private static final ExecutorService executor = new ThreadPoolExecutor(4, 15,
            50L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));

    /**
     * ��ɱ���
     * @param userid �û�id
     */
    public static boolean secKillRequest(String userid){
        if (stock <= 0) return false;

        //�ۿ�棬��֤�̰߳�ȫ
        synchronized (HandleHighConcurrencyDemo1.class) {
            if (stock > 0){
                stock--;
                System.out.println(userid + " �û������ɹ���ʣ���� " + stock);
                return true;
            }
        }
        return false;
    }

    /**
     * ģ��10���û�����
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            final String userid = "User-" + i;
            executor.submit(()-> secKillRequest(userid));
        }
        executor.shutdown();
    }
}
