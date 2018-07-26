package com.shindo.redis.com.shindo.demo;
import redis.clients.jedis.Jedis;


/**
 * Created by shindo.yang on 2018/7/19.
 */
public class TestRedis {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        String test = jedis.get("mykey");
        System.out.println(test);
    }


}
