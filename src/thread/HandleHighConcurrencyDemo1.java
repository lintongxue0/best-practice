package thread;

import java.util.concurrent.*;

/**
 * 模拟处理高并发实践
 * 场景：秒杀有限库存，多线程处理
 * 缺点：
 *  1.volatile只能保证可见性，不能保证该变量的原子性，
 *  2.synchronized使用类锁，锁的粒度大，并发性能较低
 */
public class HandleHighConcurrencyDemo1 {
    // volatile 作用：
    //   1. 防止CPU指令重排
    //   2. 变量可见性，线程修改该变量会立即更新到主存（因为线程也有自己的缓存，一般是在主存复制一份到自己的缓存操作）
    private volatile static int stock = 10;

    // 创建线程池（核心10，最大50，空闲存活时间50秒，阻塞队列容量1000）
    private static final ExecutorService executor = new ThreadPoolExecutor(4, 15,
            50L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(1000));

    /**
     * 秒杀库存
     * @param userid 用户id
     */
    public static boolean secKillRequest(String userid){
        if (stock <= 0) return false;

        //扣库存，保证线程安全
        synchronized (HandleHighConcurrencyDemo1.class) {
            if (stock > 0){
                stock--;
                System.out.println(userid + " 用户抢购成功，剩余库存 " + stock);
                return true;
            }
        }
        return false;
    }

    /**
     * 模拟10万用户抢购
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
