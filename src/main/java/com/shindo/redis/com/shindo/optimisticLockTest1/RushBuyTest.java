package com.shindo.redis.com.shindo.optimisticLockTest1;

import com.shindo.redis.com.shindo.optimisticLockTest1.MyRunnable;
import redis.clients.jedis.Jedis;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * redis测试抢购
 * 1、使用watch，采用乐观锁
 * 2、不使用悲观锁，因为等待时间非常长，响应慢
 * 3、不使用队列，因为并发量会让队列内存瞬间升高
 *
 * @author 10255_000
 */
public class RushBuyTest {
    public static void main(String[] args) {
        final String watchkeys = "watchkeys";
        ExecutorService executor = Executors.newFixedThreadPool(20);

        final Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.set(watchkeys, "0");// 重置watchkeys为0
        jedis.del("setsucc", "setfail");// 清空抢成功的，与没有成功的
        jedis.close();

        for (int i = 0; i < 10000; i++) {// 测试一万人同时访问
            executor.execute(new MyRunnable());
        }

        executor.shutdown();
    }
}
