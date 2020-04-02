package com.cl.learn.redis.model.entity;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

@Data
public class Item implements Serializable{
    private Integer id;

    @NotBlank(message = "商品编码不能为空！")
    private String code;

    @NotBlank(message = "商品名称不能为空！")
    private String name;

    private Date createTime;

    private String content;

}