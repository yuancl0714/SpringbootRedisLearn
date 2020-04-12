package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.entity.Product;
import com.cl.learn.redis.model.mapper.ProductMapper;
import com.cl.learn.redis.server.enums.Constant;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

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

    // 添加商品信息
    @Transactional(rollbackFor = Exception.class)
    public Integer add(Product product) throws Exception {
        // 将商品的信息补全
        product.setId(null);
        /*product.setScanTotal(0);*/
        int insert = productMapper.insertSelective(product);
        // 如果插入成功往缓存放一份商品的信息
        if (insert > 0) {
            ListOperations<String, Product> list = redisTemplate.opsForList();
            list.leftPush(Constant.RedisListPrefix + product.getUserId(), product);
        }
        return 1;
    }

    // 获取商品信息
    public List<Product> get(final Integer userId) {
        // 设置商品信息的key
        String key = Constant.RedisListPrefix + userId;
        // 从缓存中获取商品信息
        ListOperations<String, Product> list = redisTemplate.opsForList();
        List<Product> range = list.range(key, 0L, list.size(key));
        // 倒过来
        Collections.reverse(range);
        return range;
    }
}
