package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.entity.Item;
import com.cl.learn.redis.model.mapper.ItemMapper;
import com.cl.learn.redis.server.enums.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class StringService {
    // 搭建日志
    private static final Logger log = LoggerFactory.getLogger(StringService.class);

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // 新增方法
    // 做一个回滚，如果缓存出现问题的话
    @Transactional(rollbackFor = Exception.class)
    public Integer add(Item item) throws Exception {
        // 加入item中缺少的参数
        item.setCreateTime(new Date());
        item.setId(null);
        int insert = itemMapper.insert(item);
        log.info("---insert={},item.getId={}", insert, item.getId());
        // 数据库写入成功，同时也向Redis写入一份，保证双写的一致性
        if (insert > 0) {
            // 将此数据写入到Redis一份
            ValueOperations value = redisTemplate.opsForValue();
            value.set(Constant.RedisStringPrefix + item.getId(), objectMapper.writeValueAsString(item));
        }
        return item.getId();
    }

    // 获取方法
    public Item get(final Integer id) throws Exception {
        /*// 将此数据写入到Redis一份
        ValueOperations value = redisTemplate.opsForValue();
        value.get(Constant.RedisStringPrefix + id);*/
        return itemMapper.selectByPrimaryKey(id);
    }
}
