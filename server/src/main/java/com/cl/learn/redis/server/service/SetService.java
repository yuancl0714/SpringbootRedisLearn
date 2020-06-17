package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.entity.Problem;
import com.cl.learn.redis.model.mapper.ProblemMapper;
import com.cl.learn.redis.model.mapper.UserMapper;
import com.cl.learn.redis.server.enums.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author chenLei
 * @date 2020/6/15 19:28
 */
@Service
public class SetService {
    private static final Logger log = LoggerFactory.getLogger(SetService.class);

    private static final Integer problemTotal = 10;
    @Autowired
    private ProblemService problemService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProblemMapper problemMapper;

    // 从缓存中取出数据
    public Set<Problem> getRandomProblem() {
        return problemService.getProblems(problemTotal);
    }

    // 添加试题
    public Integer addProblem(Problem problem) {
        problem.setId(null);
        // 先往db中放一份，再往Redis中放一份
        int i = problemMapper.insertSelective(problem);
        if (i > 0) {
            // 方式一
            //problemService.initProblems();

            // 方式二
            SetOperations setOperations = redisTemplate.opsForSet();
            setOperations.add(Constant.RedisSetProblemKey,problem);
        }
        return problem.getId();
    }
}
