package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.entity.PhoneFare;
import com.cl.learn.redis.model.mapper.PhoneFareMapper;
import com.cl.learn.redis.server.enums.Constant;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

/**
 * @author chenLei
 * @date 2020/6/18 12:47
 */
@Service
public class SortedSetService {
    // 日志文件
    private static final Logger log = LoggerFactory.getLogger(SortedSetService.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PhoneFareMapper phoneFareMapper;

    // 获取手机充值记录
    public List<PhoneFare> getPhoneFare() {
        List<PhoneFare> list = Lists.newLinkedList();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        // 获取排好序的带分数的成员
        Set<ZSetOperations.TypedTuple<String>> set = zSetOperations.rangeWithScores(Constant.RedisSortedSetKey2, 0, zSetOperations.size(Constant.RedisSortedSetKey2));
        if (set != null && !set.isEmpty()){
            set.forEach(tuple -> list.add(new PhoneFare(tuple.getValue(), BigDecimal.valueOf(tuple.getScore()))));
        }
        return list;
    }

    // 添加手机充值记录
    @Transactional(rollbackFor = Exception.class)
    public Integer put(PhoneFare phoneFare) {
        phoneFare.setId(null);
        int i = phoneFareMapper.insertSelective(phoneFare);
        if (i > 0) {
            ZSetOperations zSetOperations = redisTemplate.opsForZSet();
            zSetOperations.add(Constant.RedisSortedSetKey2, phoneFare.getPhone(),
                    phoneFare.getFare().doubleValue());
        }
        return phoneFare.getId();
    }
}
