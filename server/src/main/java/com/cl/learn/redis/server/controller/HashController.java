package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import com.cl.learn.redis.model.entity.SysConfig;
import com.cl.learn.redis.server.service.HashService;
import com.cl.learn.redis.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenLei
 * @date 2020/6/18 18:49
 */
@RestController
@RequestMapping(value = "hash")
public class HashController {
    // 日志文件
    private static final Logger log = LoggerFactory.getLogger(HashController.class);

    @Autowired
    private HashService hashService;

    //添加数据字典
    @RequestMapping(value = "put", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse put(@RequestBody @Validated SysConfig sysConfig, BindingResult result) {
        BaseResponse response = new BaseResponse(StatusCode.Success);

        // 获取错误信息
        String s = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(s)) {
            response = new BaseResponse(StatusCode.Fail.getCode(), s);
        }
        try {
            // 添加数据字典
            response.setData(hashService.put(sysConfig));
        } catch (Exception e) {
            log.info("---添加数据字典失败---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    // 获取所有数据字典
    @RequestMapping(value = "getAll",method = RequestMethod.GET)
    public BaseResponse getAll(){
        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            // 添加数据字典
            response.setData(hashService.getAll());
        } catch (Exception e) {
            log.info("---添加数据字典失败---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    // 获取指定的数据字典及其选项
    @RequestMapping(value = "getOne",method = RequestMethod.GET)
    public BaseResponse getOne(@RequestParam String type){
        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            // 添加数据字典
            response.setData(hashService.getOne(type));
        } catch (Exception e) {
            log.info("---添加数据字典失败---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}
