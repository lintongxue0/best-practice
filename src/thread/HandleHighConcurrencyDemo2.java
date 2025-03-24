package thread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟处理高并发实践
 * 场景：秒杀有限库存，多线程处理
 * 改进：
 * 1.使用juc包的AtomicInteger保证库存操作原子性，减小锁的粒度
 * 2.采用cas操作替代 synchronized，减少线程竞争。
 */
public class HandleHighConcurrencyDemo2 {
    private static final AtomicInteger stock = new AtomicInteger(10);

    // 创建线程池（核心5，最大15，空闲存活时间50秒，阻塞队列容量1000）
    private static final ExecutorService executor = new ThreadPoolExecutor(
            5, 15,
            50L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(1000),
            new ThreadPoolExecutor.AbortPolicy() //由提交线程执行任务（主线程）
    );

    /**
     * 秒杀库存
     *
     * @param userid 用户id
     */
    public static boolean secKillRequest(String userid) {
        int currentStock;
        int newStock;
        do {
            currentStock = stock.get();
            if (currentStock <= 0) return false;
            newStock = currentStock - 1;
        } while (!stock.compareAndSet(currentStock, newStock));

        System.out.println(userid + " 抢购成功，剩余库存 " + newStock); // 记录操作时的准确值
        return true;
    }


    /**
     * 模拟10万用户抢购
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
