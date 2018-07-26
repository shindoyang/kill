package com.shindo.redis.com.shindo.optimisticLockTest2;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientThread implements Runnable {
    Jedis jedis = null;
    String key = "prdNum";// 商品主键
    String clientList = "clientList";//// 抢购到商品的顾客列表主键
    String clientName;

    public ClientThread(int num) {
        clientName = "编号=" + num;
    }

    private static BlockingQueue task = new LinkedBlockingDeque(1000);

    static int i = 0;
    public static BlockingQueue getTask(){
        System.out.println(" get task ---------------------" + i++);
        return task;
    }


    public void run() {
        try {
            Thread.sleep((int)(Math.random()*5000));// 随机睡眠一下
        } catch (InterruptedException e1) {
        }
        while (true) {
            System.out.println("顾客:" + clientName + "开始抢商品");
            jedis = RedisUtil.getInstance().getJedis();
            try {
                jedis.watch(key);
                int prdNum = Integer.parseInt(jedis.get(key));// 当前商品个数
                if (prdNum > 0) {
                    Transaction transaction = jedis.multi();
                    transaction.set(key, String.valueOf(prdNum - 1));
                    List<Object> result = transaction.exec();
                    if (result == null || result.isEmpty()) {
                        System.out.println("悲剧了，顾客:" + clientName + "没有抢到商品");// 可能是watch-key被外部修改，或者是数据操作被驳回
                    } else {
                        jedis.sadd(clientList, clientName);// 抢到商品记录一下
                        System.out.println("好高兴，顾客:" + clientName + "抢到商品");
                        task.put(clientName);
                        break;
                    }
                } else {
                    System.out.println("悲剧了，库存为0，顾客:" + clientName + "没有抢到商品");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                jedis.unwatch();
                RedisUtil.returnResource(jedis);
            }

        }
    }

}
