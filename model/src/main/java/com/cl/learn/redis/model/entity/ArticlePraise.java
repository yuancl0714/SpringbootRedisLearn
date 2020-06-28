package com.cl.learn.redis.model.entity;

import java.util.Date;

public class ArticlePraise {
    private Integer id;

    private Integer articleId;

    private Integer userId;

    private Date pTime;

    private Date updateTime;

    public ArticlePraise(Integer id, Integer articleId, Integer userId, Date pTime, Date updateTime) {
        this.id = id;
        this.articleId = articleId;
        this.userId = userId;
        this.pTime = pTime;
        this.updateTime = updateTime;
    }

    public ArticlePraise(Integer articleId, Integer userId, Date pTime) {
        this.articleId = articleId;
        this.userId = userId;
        this.pTime = pTime;
    }

    public ArticlePraise() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getpTime() {
        return pTime;
    }

    public void setpTime(Date pTime) {
        this.pTime = pTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}