package com.cl.learn.redis.test;

import com.cl.learn.redis.server.MainApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
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

    @Test
    public void method3() {
        log.info("------开始列表set测试------");

        // 定义key值
        final String key1 = "StringBootRedis:set:10010:V1";
        final String key2 = "StringBootRedis:set:10011:V1";
        redisTemplate.delete(key1);
        redisTemplate.delete(key2);

        SetOperations<String, String> set = redisTemplate.opsForSet();
        set.add(key1, new String[]{"a", "b", "c"});
        set.add(key2, new String[]{"b", "e", "f"});

        log.info("---集合key1的元素：{}", set.members(key1));
        log.info("---集合key2的元素：{}", set.members(key2));

        log.info("---集合key1随机取1个元素：{}", set.randomMember(key1));
        log.info("---集合key1随机取n个元素：{}", set.randomMembers(key1, 3));

        log.info("---集合key1的元素个数：{}", set.size(key1));
        log.info("---集合key2的元素个数：{}", set.size(key2));

        log.info("---元素a是否为集合key1的元素：{}", set.isMember(key1, "a"));
        log.info("---元素f是否为集合key1的元素：{}", set.isMember(key1, "f"));

        log.info("---集合key1和集合key2的差集元素：{}", set.difference(key1, key2));
        log.info("---集合key1和集合key2的并集元素：{}", set.union(key1, key2));
        log.info("---集合key1和集合key2的交集元素：{}", set.intersect(key1, key2));

        log.info("---从集合key1中弹出一个随机元素：{}", set.pop(key1));
        log.info("---集合key1的元素：{}", set.members(key1));
        log.info("---将c从集合key1的元素列表中移除：{}", set.remove(key1, "c"));
    }

}
