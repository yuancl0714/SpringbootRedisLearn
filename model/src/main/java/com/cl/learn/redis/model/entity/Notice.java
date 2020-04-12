package com.cl.learn.redis.model.entity;

import org.hibernate.validator.constraints.NotBlank;

public class Notice {
    private Integer id;

    @NotBlank(message = "通告标题必填")
    private String title;

    @NotBlank(message = "通告内容必填")
    private String content;

    private Byte isActive = 1;

    public Notice(Integer id, String title, String content, Byte isActive) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isActive = isActive;
    }

    public Notice() {
        super();
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

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }
}