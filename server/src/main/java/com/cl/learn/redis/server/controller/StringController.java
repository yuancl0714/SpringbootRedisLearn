package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import com.cl.learn.redis.model.entity.Item;
import com.cl.learn.redis.server.service.StringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "string/item")
public class StringController {
    // 搭建日志
    private static final Logger log = LoggerFactory.getLogger(StringController.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private StringService stringService;

    // 新增商品信息
    @RequestMapping(value = "add", method = RequestMethod.POST, consumes =
            MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse add(@RequestBody @Validated Item item, BindingResult result) {
        // 如果参数有问题统一处理
        if (result.hasErrors()) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            // 将信息写入到数据库中
            response.setData(stringService.add(item));
        } catch (Exception e) {
            log.error("商品对象信息管理-缓存-新增-异常信息：", e);
            response = new BaseResponse(StatusCode.Fail);
        }
        return response;
    }

    // 获取商品信息
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public BaseResponse get(@RequestParam("id") Integer id) {
        // 如果参数有问题统一处理
        if (id <= 0) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            // 读取商品信息
            response.setData(stringService.get(id));
        } catch (Exception e) {
            log.error("商品对象信息管理-缓存-详情-异常信息：", e);
            response = new BaseResponse(StatusCode.Fail);
        }
        return response;
    }

    // 修改商品信息
    @RequestMapping(value = "update", method = RequestMethod.PUT)
    public BaseResponse update(@RequestBody @Validated Item item, BindingResult result) {
        if (result.hasErrors()) {
            return new BaseResponse(StatusCode.InvalidParams);
        }
        // 如果参数有问题统一处理
        if (item.getId() <= 0 || item.getId() == null) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            response.setData(stringService.update(item));
        } catch (Exception e) {
            log.error("商品对象信息管理-缓存-修改-异常信息：", e);
            response = new BaseResponse(StatusCode.Fail);
        }
        return response;
    }

    // 删除商品信息
    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    public BaseResponse delete(@RequestParam("id") Integer id) {
        if (id <= 0) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            stringService.delete(id);
        } catch (Exception e) {
            log.error("商品对象信息管理-缓存-删除-异常信息：", e);
            response = new BaseResponse(StatusCode.Fail);
        }
        return response;
    }
}
