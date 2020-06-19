package com.cl.learn.redis.model.entity;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

public class SysConfig implements Serializable {
    private Integer id;

    @NotBlank(message = "字典类型不能为空！")
    private String type;

    @NotBlank(message = "字典名称不能为空！")
    private String name;

    @NotBlank(message = "选项编码不能为空！")
    private String code;

    @NotBlank(message = "选项取值不能为空！")
    private String value;

    private Integer orderBy;

    private Integer isActive;

    private Date createTime;

    public SysConfig(Integer id, String type, String name, String code, String value, Integer orderBy, Integer isActive, Date createTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.code = code;
        this.value = value;
        this.orderBy = orderBy;
        this.isActive = isActive;
        this.createTime = createTime;
    }

    public SysConfig() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
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
}