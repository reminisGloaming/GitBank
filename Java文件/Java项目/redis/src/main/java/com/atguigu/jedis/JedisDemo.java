package com.atguigu.jedis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class JedisDemo {

    public static void main(String[] args) {

//        创建jedis对象
      //  Jedis jedis = new Jedis("192.168.7.132",6379);

//        测试
//        String value = jedis.ping();
//        System.out.println(value);
    }

    @Test
    public void demo1(){
        Jedis jedis = new Jedis("192.168.7.132",6379);

        Set<String> keys = jedis.keys("*");
//        for (String key:keys
//             ) {
//            System.out.println(key);
//
//        }

       jedis.mset("k1", "v1", "k2", "v2");
        List<String> mget = jedis.mget("k1", "k2");
        System.out.println(mget);
    }
}


