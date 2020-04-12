package com.cl.learn.redis.model.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


public class Product implements Serializable {
    private Integer id;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    @NotNull(message = "所属商户id不能为空")
    private Integer userId;

    private Integer scanTotal = 0;

    private Byte isActive = 1;

    public Product(Integer id, String name, Integer userId, Integer scanTotal, Byte isActive) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.scanTotal = scanTotal;
        this.isActive = isActive;
    }

    public Product() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
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

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }
}