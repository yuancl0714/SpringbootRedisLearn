package com.cl.learn.redis.model.mapper;

import com.cl.learn.redis.model.entity.ArticlePraise;
import org.apache.ibatis.annotations.Param;

public interface ArticlePraiseMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ArticlePraise record);

    int insertSelective(ArticlePraise record);

    ArticlePraise selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ArticlePraise record);

    int updateByPrimaryKey(ArticlePraise record);

    Integer cancelPraise(@Param("articleId") Integer articleId, @Param("userId") Integer userId);
}