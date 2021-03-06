package com.cl.learn.redis.model.mapper;

import com.cl.learn.redis.model.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    List<User> selectList();

    User selectByEmail(@Param("email") String email);

    String selectNamesById(@Param("ids") String ids);

    String selectNames(@Param("set")Set<Integer> set);
}