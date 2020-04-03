package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.entity.Item;
import com.cl.learn.redis.model.mapper.ItemMapper;
import com.cl.learn.redis.server.enums.Constant;
import com.fasterxml.jackson.core.JsonProcessingException;
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
        // 方法中的对象
        Item item = null;
        String key = Constant.RedisStringPrefix + id;

        // 判断缓存中有没有内容
        if (redisTemplate.hasKey(key)) {
            ValueOperations value = redisTemplate.opsForValue();
            Object o = value.get(key);
            if (o != null) {
                item = objectMapper.readValue(o.toString(), Item.class);
            }
        } else {
            item = itemMapper.selectByPrimaryKey(id);
            // 从数据库查询出来之后往缓存中也放一份
            ValueOperations value = redisTemplate.opsForValue();
            if (item == null) {
                // 设置为空，为了防止缓存穿透
                value.set(key, "");
            } else {
                value.set(key, objectMapper.writeValueAsString(item));
            }
        }
        return item;
    }

    // 修改商品信息
    @Transactional(rollbackFor = Exception.class)
    public Integer update(Item item) {
        // 修改数据库的信息
        int i = itemMapper.updateByPrimaryKey(item);
        String key = Constant.RedisStringPrefix + item.getId();
        if (i > 0) {
            try {
                // 将修改后的信息重新放到缓存中
                ValueOperations value = redisTemplate.opsForValue();
                value.set(key, objectMapper.writeValueAsString(item));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return i;
    }

    // 删除商品信息
    public Integer delete(final Integer id) {
        int i = itemMapper.deleteByPrimaryKey(id);
        // 判断缓存中有没有该数据
        redisTemplate.delete(Constant.RedisStringPrefix + id);
        return i;
    }
}
