package com.cl.learn.redis.model.entity;

import org.hibernate.validator.constraints.NotBlank;

public class Problem {
    private Integer id;

    @NotBlank(message = "商品标题不能为空！")
    private String title;

    private String choiceA;

    private String choiceB;

    private Integer isActive;

    private Integer orderBy;

    public Problem(Integer id, String title, String choiceA, String choiceB, Integer isActive, Integer orderBy) {
        this.id = id;
        this.title = title;
        this.choiceA = choiceA;
        this.choiceB = choiceB;
        this.isActive = isActive;
        this.orderBy = orderBy;
    }

    public Problem() {
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

    public String getChoiceA() {
        return choiceA;
    }

    public void setChoiceA(String choiceA) {
        this.choiceA = choiceA == null ? null : choiceA.trim();
    }

    public String getChoiceB() {
        return choiceB;
    }

    public void setChoiceB(String choiceB) {
        this.choiceB = choiceB == null ? null : choiceB.trim();
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }
}