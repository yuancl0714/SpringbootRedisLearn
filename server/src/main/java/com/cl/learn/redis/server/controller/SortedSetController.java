package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import com.cl.learn.redis.model.entity.PhoneFare;
import com.cl.learn.redis.server.service.SortedSetService;
import com.cl.learn.redis.server.utils.ValidatorUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

/**
 * @author chenLei
 * @date 2020/6/18 12:46
 */
@RestController
@RequestMapping(value = "sorted/set")
public class SortedSetController {
    // 日志文件
    private static final Logger log = LoggerFactory.getLogger(SortedSetController.class);

    @Autowired
    private SortedSetService sortedSetService;

    // 获取手机充值记录
    @RequestMapping(value = "rank", method = RequestMethod.GET)
    public BaseResponse getPhoneFare() {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        HashMap<String, Object> map = Maps.newHashMap();

        try {
            response.setData(sortedSetService.getPhoneFare());
        } catch (Exception e) {
            log.info("---获取手机充值记录失败---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    // 添加手机充值记录
    @RequestMapping(value = "put", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse put(@RequestBody @Validated PhoneFare phoneFare, BindingResult result) {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        // 获取出错信息
        String checkResult = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(checkResult)) {
            return new BaseResponse(StatusCode.Fail.getCode(), checkResult);
        }

        try {
            response.setData(sortedSetService.put(phoneFare));
        } catch (Exception e) {
            log.info("---添加手机充值记录失败---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

}
