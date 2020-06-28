package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.dto.PraiseDto;
import com.cl.learn.redis.model.entity.Article;
import com.cl.learn.redis.model.entity.ArticlePraise;
import com.cl.learn.redis.model.mapper.ArticleMapper;
import com.cl.learn.redis.model.mapper.ArticlePraiseMapper;
import com.cl.learn.redis.server.enums.Constant;
import com.google.common.collect.Sets;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author chenLei
 * @date 2020/6/19 18:46
 */
@Service
public class PraiseService {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticlePraiseMapper articlePraiseMapper;

    // 获取文章列表
    public List<Article> getArticleList() throws Exception {
        // 从数据库中获取文章列表
       /* // 如果文章列表不为空则往缓存中放一份
        if (articles != null && articles.isEmpty()) {
            ListOperations<String,Article> listOperations = redisTemplate.opsForList();
        }*/
        return articleMapper.getArticleList();
    }

    public Boolean praiseOn(PraiseDto dto) throws Exception {
        // 判断当前用户是否已经点赞过该文章
        Boolean absent = redisTemplate.opsForValue().setIfAbsent(Constant.RedisArticlePraiseUser + dto.getArticleId() + dto.getUserId(), 1);
        if (absent) {
            // 首先将点赞的记录插入到db中
            ArticlePraise praise = new ArticlePraise(dto.getArticleId(), dto.getUserId(), DateTime.now().toDate());
            int i = articlePraiseMapper.insertSelective(praise);
            if (i > 0) {
                // 叠加当前文章的点赞总量
                articleMapper.updatePraiseTotal(dto.getArticleId(), 1);
                // 缓存点赞的信息
                this.cachePraiseOn(dto);
            }
        }
        return true;
    }

    // 缓存点赞的信息
    private void cachePraiseOn(final PraiseDto dto) throws Exception {
        // 创建redis的hash数据结构
        HashOperations<String, String, Set<Integer>> hash = redisTemplate.opsForHash();

        // 判断原先是否有用户点赞过该文章
        Set<Integer> set = hash.get(Constant.RedisArticlePraiseHashKey, dto.getArticleId().toString());
        if (set == null || set.isEmpty()) {
            set = Sets.newHashSet();
        }

        set.add(dto.getUserId());
        hash.put(Constant.RedisArticlePraiseHashKey, dto.getArticleId().toString(), set);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean praiseCancel(PraiseDto dto) {
        // 判断当前用户是否点赞过当前文章
        Boolean key = redisTemplate.hasKey(Constant.RedisArticlePraiseUser + dto.getArticleId() + dto.getUserId());
        if (key) {
            // 删除db中的点赞记录
            int num = articlePraiseMapper.cancelPraise(dto.getArticleId(), dto.getUserId());
            if (num > 0) {
                // 移除缓存中的:是否点赞的key
                redisTemplate.delete(Constant.RedisArticlePraiseUser + dto.getArticleId() + dto.getUserId());
                // 更新文章中的点赞量
                articleMapper.updatePraiseTotal(dto.getArticleId(), -1);

                // 缓存取消点赞的相关信息
                this.cachePraiseCancel(dto);
            }
        }
        return true;
    }

    // 缓存取消点赞的相关信息
    public void cachePraiseCancel(final PraiseDto dto) {
        HashOperations<String, String, Set<Integer>> hash = redisTemplate.opsForHash();

        // 从缓存中取出当前文章的点赞数据
        Set<Integer> set = hash.get(Constant.RedisArticlePraiseHashKey, dto.getArticleId().toString());
        if (set != null && !set.isEmpty() && set.contains(dto.getUserId())) {
            // 移除缓存中的数据
            set.remove(dto.getUserId());
            // 将新的数据放回到缓存中
            hash.put(Constant.RedisArticlePraiseHashKey, dto.getArticleId().toString(), set);
        }

    }
}
