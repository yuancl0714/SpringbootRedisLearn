package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import com.cl.learn.redis.model.entity.Item;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "string")
public class StringController {

    @RequestMapping(value = "add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse add(@RequestBody @Validated Item item) {

        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {

        } catch (Exception e) {
            response = new BaseResponse(StatusCode.Fail);
        }
        return response;
    }
}
