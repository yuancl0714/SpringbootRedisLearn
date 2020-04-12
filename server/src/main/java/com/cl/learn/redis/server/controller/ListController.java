package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import com.cl.learn.redis.model.entity.Notice;
import com.cl.learn.redis.model.entity.Product;
import com.cl.learn.redis.server.service.ProductService;
import com.cl.learn.redis.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenLei
 * @date 2020/4/4 21:44
 */
@RestController
@RequestMapping(value = "list")
public class ListController extends AbstractController {

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
    @RequestMapping(value = "add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
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
     * 获取详情
     *
     * @return BaseResponse
     */
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BaseResponse get(@RequestParam("userId") final Integer userId) {
        // 申明一个返回类
        BaseResponse response = new BaseResponse(StatusCode.Success);

        // 获取商品信息
        try {
            List<Product> list = productService.get(userId);
            response.setData(list);
        } catch (Exception e) {
            log.info("---------获取商品信息异常--------", e);
            e.printStackTrace();
        }
        return response;
    }

    // 平台管理员添加管理公告并发送通知给各位用户
    @RequestMapping(value = "notice/put", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse putNotice(@RequestBody @Validated Notice notice, BindingResult result) {
        // 参数问题统一处理
        String checkResult = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkResult)) {
            return new BaseResponse(StatusCode.Fail.getCode(), checkResult);
        }

        // 申明一个返回类
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("---平台发送通知给各位用户：{}", notice);

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;

    }
}
