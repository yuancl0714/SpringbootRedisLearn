package com.cl.learn.redis.test;

import com.cl.learn.redis.server.MainApplication;
import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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

    @Test
    public void method4() {
        log.info("------开始列表SortedSet测试------");

        // 定义key值
        final String key = "StringBootRedis:SortedSet:10010:V1";
        redisTemplate.delete(key);

        ZSetOperations opsForZSet = redisTemplate.opsForZSet();
        opsForZSet.add(key, "a", 8.0);
        opsForZSet.add(key, "b", 2.0);
        opsForZSet.add(key, "c", 4.0);
        opsForZSet.add(key, "d", 6.0);
        Long size = opsForZSet.size(key);
        log.info("---有序集合SortedSet的成员数：{}", size);
        log.info("---有序集合SortedSet-按照分数正序：{}", opsForZSet.range(key, 0L, size));

        log.info("---有序集合SortedSet-按照分数倒序：{}", opsForZSet.reverseRange(key, 0L, size));
        log.info("---有序集合SortedSet-获取成员a的分数：{}", opsForZSet.score(key, "a"));

        log.info("---有序集合SortedSet-获取成员c的分数：{}", opsForZSet.score(key, "c"));
        log.info("---有序集合SortedSet-正序中c的排名：{}", opsForZSet.rank(key, "c"));
        log.info("---有序集合SortedSet-倒序中c的排名：{}", opsForZSet.reverseRank(key, "c"));
        opsForZSet.incrementScore(key, "b", 8.0);
        log.info("---为元素b加十分后，有序集合SortedSet-按照分数倒序：{}", opsForZSet.reverseRange(key, 0L, size));

        opsForZSet.remove(key, "b");
        log.info("---移除元素b后，有序集合SortedSet-按照分数倒序：{}", opsForZSet.reverseRange(key, 0L, size));
        log.info("---有序集合SortedSet-取出分数区间0-7的成员：{}", opsForZSet.rangeByScore(key, 0, 7));
        log.info("---有序集合SortedSet-取出带分数的排好序的成员：");
        Set<ZSetOperations.TypedTuple<String>> set = opsForZSet.rangeWithScores(key, 0L, size);
        set.forEach(tuple -> log.info("--当前成员：{}，对应分数：{}", tuple.getValue(), tuple.getScore()));
    }

    @Test
    public void method5() {
        log.info("------开始列表Hash测试------");

        // 定义key值
        final String key = "StringBootRedis:Hash:key:V1";
        redisTemplate.delete(key);

        HashOperations<String, String, String> hash = redisTemplate.opsForHash();
        hash.put(key, "10010", "zhangsan");
        hash.put(key, "10011", "lisi");
        hash.put(key, "10012", "wangwu");
        Map<String, String> map = Maps.newHashMap();
        map.put("10013", "zhaoliu");
        hash.putAll(key, map);

        log.info("--哈希hash-获取列表元素：{}", hash.entries(key));
        log.info("--哈希hash-获取10012的元素：{}", hash.get(key, "10012"));
        log.info("--哈希hash-获取所有元素field列表：{}", hash.keys(key));
        log.info("--哈希hash-10013成员是否存在：{}", hash.hasKey(key, "10013"));
        log.info("--哈希hash-10014成员是否存在：{}", hash.hasKey(key, "10014"));
        log.info("--哈希hash-删除元素10010 10011：{}", hash.delete(key, "10010", "10011"));
        log.info("--哈希hash-获取列表元素：{}", hash.entries(key));
        log.info("--哈希hash-获取列表元素个数：{}", hash.size(key));

    }
}
