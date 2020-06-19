package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenLei
 * @date 2020/6/19 17:29
 */
@RestController
@RequestMapping(value = "expire")
public class KeyExpireController extends AbstractController {

    @RequestMapping(value = "put")
    public BaseResponse put(@RequestParam String orderNo) {
        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    @RequestMapping(value = "info")
    public BaseResponse info(@RequestParam String orderNo) {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}
