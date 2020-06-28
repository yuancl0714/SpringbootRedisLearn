package com.cl.learn.redis.model.entity;

import java.util.Date;

public class Article {
    private Integer id;

    private String title;

    private String content;

    private Integer userId;

    private Integer scanTotal;

    private Integer praiseTotal;

    private Integer isActive;

    private Date createTime;

    private Date updateTime;

    private String userName;

    public Article(Integer id, String title, String content, Integer userId, Integer scanTotal, Integer praiseTotal, Integer isActive, Date createTime, Date updateTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.scanTotal = scanTotal;
        this.praiseTotal = praiseTotal;
        this.isActive = isActive;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScanTotal() {
        return scanTotal;
    }

    public void setScanTotal(Integer scanTotal) {
        this.scanTotal = scanTotal;
    }

    public Integer getPraiseTotal() {
        return praiseTotal;
    }

    public void setPraiseTotal(Integer praiseTotal) {
        this.praiseTotal = praiseTotal;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}