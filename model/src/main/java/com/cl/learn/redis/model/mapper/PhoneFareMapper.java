package com.cl.learn.redis.model.mapper;

import com.cl.learn.redis.model.entity.PhoneFare;

import java.util.SortedSet;

public interface PhoneFareMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PhoneFare record);

    int insertSelective(PhoneFare record);

    PhoneFare selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PhoneFare record);

    int updateByPrimaryKey(PhoneFare record);

    SortedSet<PhoneFare> getPhoneFare();
}