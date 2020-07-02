package com.cl.learn.redis.server.controller;

import com.cl.learn.redis.api.response.BaseResponse;
import com.cl.learn.redis.api.response.StatusCode;
import com.cl.learn.redis.model.dto.PraiseDto;
import com.cl.learn.redis.server.service.PraiseService;
import com.cl.learn.redis.server.utils.ValidatorUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chenLei
 * @date 2020/6/19 18:40
 */
@RestController
@RequestMapping(value = "praise")
public class PraiseController extends AbstractController {

    private static final Logger log = LoggerFactory.getLogger(PraiseController.class);

    @Autowired
    private PraiseService praiseService;

    // 获取文章列表
    @RequestMapping(value = "getArticleList")
    public BaseResponse getArticleList() {
        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            response.setData(praiseService.getArticleList());
        } catch (Exception e) {
            log.info("---获取文章列表失败---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    // 点赞文章
    @RequestMapping(value = "praiseArticle", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse praiseArticle(@RequestBody @Validated PraiseDto dto, BindingResult result) {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        // 获取错误数据
        String s = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(s)) {
            return new BaseResponse(StatusCode.Fail.getCode(), s);
        }

        try {
            response.setData(praiseService.praiseOn(dto));
        } catch (Exception e) {
            log.info("---点赞文章失败---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }


    // 取消点赞
    @RequestMapping(value = "praiseCancel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse praiseCancel(@RequestBody @Validated PraiseDto dto, BindingResult result) {
        BaseResponse response = new BaseResponse(StatusCode.Success);
        // 获取错误数据
        String s = ValidatorUtil.checkResult(result);
        if (StringUtils.isNotBlank(s)) {
            return new BaseResponse(StatusCode.Fail.getCode(), s);
        }

        try {
            response.setData(praiseService.praiseCancel(dto));
        } catch (Exception e) {
            log.info("---点赞文章失败---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    // 获取文章详情~排行榜
    @RequestMapping(value = "getArticles", method = RequestMethod.GET)
    public BaseResponse getArticles(@RequestParam Integer articleId, Integer curUserId) {
        // 校验数据
        if (articleId == null || articleId < 0) {
            return new BaseResponse(StatusCode.InvalidParams);
        }

        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            response.setData(praiseService.getArticleInfo(articleId, curUserId));
        } catch (Exception e) {
            log.info("---获取文章详情~排行榜---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

    // 获取用户点赞过的历史文章
    @RequestMapping(value = "user/articles", method = RequestMethod.GET)
    public BaseResponse getUserArticles(@RequestParam Integer curUserId) {

        BaseResponse response = new BaseResponse(StatusCode.Success);

        try {
            response.setData(praiseService.getUserArticles(curUserId));
        } catch (Exception e) {
            log.info("---获取文章详情~排行榜---", e);
            response = new BaseResponse(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }

}
