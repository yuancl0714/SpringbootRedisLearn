package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.entity.Problem;
import com.cl.learn.redis.model.mapper.ProblemMapper;
import com.cl.learn.redis.server.enums.Constant;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chenLei
 * @date 2020/6/15 15:15
 */
@Service
public class ProblemService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ProblemService.class);
    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // 项目启动之后加载试题资源
    @Override
    public void run(String... args) throws Exception {
        // 往Redis里面放一份
        initProblems();
    }

    // 往Redis里面放一份试题资源
    public void initProblems() {
        try {
            // 首先清空缓存中的数据
            redisTemplate.delete(Constant.RedisSetProblemKey);

            Set<Problem> set = problemMapper.getAll();
            if (set != null && !set.isEmpty()) {
                SetOperations setOperations = redisTemplate.opsForSet();
                set.forEach(problem -> setOperations.add(Constant.RedisSetProblemKey, problem));
            }
        } catch (Exception e) {
            log.error("试题初始化失败", e);
        }
    }

    // 从Redis中获取指定多少条试题资料
    public Set<Problem> getProblems(final Integer total) {
        Set<Problem> newHashSet = Sets.newHashSet();
        try {
            SetOperations<String, Problem> setOperations = redisTemplate.opsForSet();
            return setOperations.distinctRandomMembers(Constant.RedisSetProblemKey, total);

        } catch (Exception e) {
            log.error("获取试题资料失败", e);
        }
        return newHashSet;
    }

}
