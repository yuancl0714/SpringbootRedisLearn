package com.cl.learn.redis.model.mapper;

import com.cl.learn.redis.model.entity.Article;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Article record);

    int insertSelective(Article record);

    Article selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Article record);

    int updateByPrimaryKey(Article record);

    List<Article> getArticleList();

    void updatePraiseTotal(@Param("articleId") Integer articleId, @Param("flag") int flag);

    Article selectByPK(@Param("articleId") Integer articleId, @Param("curUserId") Integer curUserId);
}