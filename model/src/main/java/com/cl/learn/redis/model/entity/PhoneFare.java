package com.cl.learn.redis.model.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class PhoneFare {
    private Integer id;

    @NotBlank(message = "手机号码不能为空！")
    private String phone;

    @NotNull(message = "充值金额不能为空！")
    private BigDecimal fare;

    private Integer isActive;

    public PhoneFare(Integer id, String phone, BigDecimal fare, Integer isActive) {
        this.id = id;
        this.phone = phone;
        this.fare = fare;
        this.isActive = isActive;
    }

    public PhoneFare(String phone, BigDecimal fare) {
        this.phone = phone;
        this.fare = fare;
    }

    public PhoneFare() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }
}