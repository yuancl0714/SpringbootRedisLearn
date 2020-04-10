package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import com.cl.learn.redis.model.entity.Product;
import com.cl.learn.redis.server.service.ProductService;
import com.cl.learn.redis.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenLei
 * @date 2020/4/4 21:44
 */
@RestController
@RequestMapping(value = "list")
public class ListController {

    // 定义日志文件
    private static final Logger log = LoggerFactory.getLogger(ListController.class);

    // 申明redis模板
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductService productService;

    /**
     * 添加商品信息
     *
     * @return BaseResponse
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public BaseResponse add(@RequestBody @Validated Product product, BindingResult result) {
        // 参数问题统一处理
        String checkResult = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkResult)) {
            return new BaseResponse(StatusCode.Fail.getCode(), checkResult);
        }

        // 申明一个返回类
        BaseResponse response = new BaseResponse(StatusCode.Success);

        // 添加商品信息
        try {
            response.setData(productService.add(product));
        } catch (Exception e) {
            log.info("---------商品信息添加异常--------", e);
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 添加商品信息
     *
     * @return BaseResponse
     */
    @RequestMapping(value = "addList", method = RequestMethod.POST)
    public BaseResponse addList(@RequestBody @Validated List<Product> products, BindingResult result) {
        // 参数问题统一处理
        String checkResult = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkResult)) {
            return new BaseResponse(StatusCode.Fail.getCode(), checkResult);
        }

        // 申明一个返回类
        BaseResponse response = new BaseResponse(StatusCode.Success);

        // 添加商品信息
        try {
            response.setData(productService.addList(products));
        } catch (Exception e) {
            log.info("---------商品信息添加异常--------", e);
            e.printStackTrace();
        }
        return response;
    }
}
