package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.entity.Product;
import com.cl.learn.redis.model.mapper.ProductMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * @author chenLei
 * @date 2020/4/4 22:17
 */
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // 添加商品信息
    public Integer add(Product product) throws Exception {
        // 设置商品信息的key
        String key = "StringBootRedis:list:10010:V2";
        // 将商品的信息补全
        product.setId(null);
        product.setScanTotal(0);
        int insert = productMapper.insert(product);
        // 如果插入成功往缓存放一份商品的信息
        if (insert > 0) {
            ListOperations list = redisTemplate.opsForList();
            Long push = list.leftPush(key, objectMapper.writeValueAsString(product));
        }
        return insert;
    }
}
