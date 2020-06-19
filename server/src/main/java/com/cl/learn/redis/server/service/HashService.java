package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.entity.SysConfig;
import com.cl.learn.redis.model.mapper.SysConfigMapper;
import com.cl.learn.redis.server.enums.Constant;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author chenLei
 * @date 2020/6/18 18:58
 */
@Service
public class HashService {
    // 日志文件
    private static final Logger log = LoggerFactory.getLogger(HashService.class);

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // 实时触发db中的数据字典的缓存存储
    public void cacheSysConfigs() {
        try {

            redisTemplate.delete(Constant.RedisHashKeyConfig);
            // 从数据库中获取所有的数据字典
            List<SysConfig> sysConfigs = sysConfigMapper.getAll();

            // 将每一种数据字典放到一起
            Map<String, List<SysConfig>> map = Maps.newHashMap();
            sysConfigs.forEach(sysConfig -> {
                List<SysConfig> list = map.get(sysConfig.getType());
                if (list == null || list.isEmpty()) {
                    list = Lists.newLinkedList();
                }
                list.add(sysConfig);
                map.put(sysConfig.getType(), list);
            });
            // 存储到hash中
            HashOperations<String, String, List<SysConfig>> hash = redisTemplate.opsForHash();
            hash.putAll(Constant.RedisHashKeyConfig, map);
        } catch (Exception e) {
            log.info("---实时触发db中的数据字典的缓存存储失败---", e);
        }
    }

    //添加数据字典
    @Transactional(rollbackFor = Exception.class)
    public Integer put(SysConfig sysConfig) {
        sysConfig.setId(null);
        int i = sysConfigMapper.insertSelective(sysConfig);
        if (i > 0) {
            cacheSysConfigs();
        }
        return sysConfig.getId();
    }

    // 获取所有数据字典
    public Map<String,List<SysConfig>> getAll() {
        // 从数据库中查询并放到redis中
        HashOperations<String, String, List<SysConfig>> hash = redisTemplate.opsForHash();
        Map<String, List<SysConfig>> map = hash.entries(Constant.RedisHashKeyConfig);
        return map;
    }

    public List<SysConfig> getOne(String type) {
        List<SysConfig> list = Lists.newLinkedList();
        HashOperations<String, String, List<SysConfig>> hash = redisTemplate.opsForHash();
        list = hash.get(Constant.RedisHashKeyConfig, type);
        return list;
    }
}
