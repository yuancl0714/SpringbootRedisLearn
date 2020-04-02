package com.cl.learn.redis.model.entity;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;

public class Item {
    private Integer id;

    @NotBlank(message = "商品编码不能为空！")
    private String code;

    @NotBlank(message = "商品名称不能为空！")
    private String name;

    private Date createTime;

    private String content;

    public Item(Integer id, String code, String name, Date createTime, String content) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.createTime = createTime;
        this.content = content;
    }

    public Item() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}