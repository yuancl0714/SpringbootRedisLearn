package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import com.cl.learn.redis.model.entity.Problem;
import com.cl.learn.redis.server.service.SetService;
import com.cl.learn.redis.server.utils.ValidatorUtil;
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

/**
 * @author chenLei
 * @date 2020/6/15 15:13
 */
@RestController
@RequestMapping(value = "set/problem")
public class SetController {
    private static final Logger log = LoggerFactory.getLogger(SetController.class);

    @Autowired
    private SetService setService;


    /**
     * 获取试题资料
     */
    @RequestMapping(value = "random", method = RequestMethod.GET)
    public BaseResponse getProblems() {
        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            response.setData(setService.getRandomProblem());
        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }


    // 添加试题
    @RequestMapping(value = "put", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse put(@RequestBody @Validated Problem problem, BindingResult result) {
        String s = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(s)) {
            return new BaseResponse(StatusCode.InvalidParams.getCode(), s);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            log.info("--数据结构set添加试题--", problem);
            response.setData(setService.addProblem(problem));
        } catch (Exception e) {
            log.info("---数据结构set添加试题发生异常", e.fillInStackTrace());
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}
