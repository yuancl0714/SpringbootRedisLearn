package com.cl.learn.redis.test;

import com.cl.learn.redis.server.MainApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

// 设置运行器
@RunWith(SpringJUnit4ClassRunner.class)
// 指明启动类
@SpringBootTest(classes = MainApplication.class)
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    // 设置日志
    private static final Logger log = LoggerFactory.getLogger(RedisTest.class);

    // 测试方法
    @Test
    public void method1() {
        log.info("------开始字符串的测试-------");
        final String key = "SpringBoot:Redis:String:Key:V1";
        ValueOperations<String, Long> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, 10L);

        // 获取key，value
        log.info("--key={},value={}", key, valueOperations.get(key));

        // redis加
        Long aLong = valueOperations.increment(key, 5L);
        log.info("---key={},value={},aLong={}", key, valueOperations.get(key), aLong);

        // redis的key失效
        redisTemplate.expire(key, 30L, TimeUnit.SECONDS);
    }

    @Test
    public void method2() {
        log.info("------开始列表list测试------");

        // 定义key值
        final String key = "StringBootRedis:list:10010:V2";
        ListOperations<String, String> list = redisTemplate.opsForList();
        list.leftPushAll(key, "a", "b", "c", "d", "e", "f", "g", "k", "l", "m");

        log.info("--当前列表元素个数：{}", list.size(key));
        log.info("--当前列表所有元素：{}", list.range(key, 0, list.size(key)));
        log.info("--当前列表中下表为0的元素：{}", list.index(key, 0));
        log.info("--当前列表中下表为4的元素：{}", list.index(key, 4));
        log.info("--当前列表中下表为10的元素：{}", list.index(key, list.size(key)));
        log.info("--当前列表从右边弹出来：{}", list.rightPop(key));
        list.set(key, 0, "10010");
        log.info("--修改列表中下标为0的元素：{}", list.index(key, 0));
        log.info("--当前列表元素变更为：{}", list.range(key, 0, list.size(key)));
        list.remove(key, 0, "d");
        log.info("--移除下标为d的元素后当前列表的所有元素：{}", list.range(key, 0, list.size(key)));
    }

}
