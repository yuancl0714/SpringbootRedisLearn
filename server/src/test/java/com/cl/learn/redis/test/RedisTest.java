package com.cl.learn.redis.test;

import com.cl.learn.redis.server.MainApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        ValueOperations valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set("SpringBoot:Redis:String:Key:V1", 10L);
    }

}
