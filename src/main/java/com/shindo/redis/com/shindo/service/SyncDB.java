package com.shindo.redis.com.shindo.service;

import com.shindo.redis.com.shindo.optimisticLockTest2.ClientThread;

/**
 * Created by shindo.yang on 2018/7/20.
 */
public class SyncDB implements Runnable{

    public void run() {
        while (true){
            try {
                System.out.println("=========================come in ======================");
                String clientName = (String)ClientThread.getTask().take();
                System.out.println("从队列里取出：" + clientName + "用户。");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
