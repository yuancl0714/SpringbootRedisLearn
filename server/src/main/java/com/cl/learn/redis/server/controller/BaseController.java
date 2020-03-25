package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import com.github.pagehelper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("base")
public class BaseController {
    // 添加日志
    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @RequestMapping("hello/world")
    public BaseResponse helloWorld(String name) {

        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            if (StringUtil.isEmpty(name)) {
                name = "程实战~SpringBoot2.0技术栈与分布式中间件系列二之Redis实战与点赞功能开发\n" +
                        "程序员实战基地~";
            }
            response.setData(name);
        } catch (Exception e) {
            response.setData(StatusCode.Fail);
        }
        return response;
    }
}
