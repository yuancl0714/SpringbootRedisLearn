package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.dto.ArticlePraiseRankDto;
import com.cl.learn.redis.model.dto.PraiseDto;
import com.cl.learn.redis.model.entity.Article;
import com.cl.learn.redis.model.entity.ArticlePraise;
import com.cl.learn.redis.model.entity.User;
import com.cl.learn.redis.model.mapper.ArticleMapper;
import com.cl.learn.redis.model.mapper.ArticlePraiseMapper;
import com.cl.learn.redis.model.mapper.UserMapper;
import com.cl.learn.redis.server.enums.Constant;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    @Autowired
    private UserMapper userMapper;

    private static final String splitChar = "-";

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

        // 缓存点赞排行榜
        this.cancelArticlePraiseRank(dto, set.size());

        // 缓存用户点赞过的文章
        this.cancelUserArticlePraise(dto, true);
    }

    // 取消点赞
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
            // 缓存点赞排行榜
            this.cancelArticlePraiseRank(dto, set.size());
            // 缓存用户点赞过的文章
            this.cancelUserArticlePraise(dto, false);
        }
    }

    // 获取文章详情
    public Map<String, Object> getArticleInfo(final Integer articleId, final Integer curUserId) {
        Map<String, Object> map = Maps.newHashMap();

        // 获取文章详情
        map.put("articleInfo~文章详情", articleMapper.selectByPK(articleId, curUserId));
        map.put("userIds~用户id列表", null);
        map.put("userName~用户姓名", null);
        map.put("当前用户是否点赞过该文章", false);

        // 获取点赞过文章的用户列表~获取昵称
        HashOperations<String, String, Set<Integer>> hash = redisTemplate.opsForHash();
        Set<Integer> set = hash.get(Constant.RedisArticlePraiseHashKey, articleId.toString());
        if (set != null && !set.isEmpty()) {
            map.put("userIds~用户id列表", set);
            // 批量查询用户名
            String join = Joiner.on(",").join(set);
            String names = userMapper.selectNamesById(join);
            map.put("userName~用户姓名", names);
            // 判断当前用户是否点赞过该文章
            if (curUserId != null) {
                map.put("当前用户是否点赞过该文章", set.contains(curUserId));
            }
        }

        // 根据点赞数的高低得到排行榜
        LinkedList<ArticlePraiseRankDto> list = Lists.newLinkedList();
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
        Long size = zSet.size(Constant.RedisArticlePraiseSortKey);
        if (size != null) {
            Set<String> range = zSet.reverseRange(Constant.RedisArticlePraiseSortKey, 0L, size);
            if (range != null && !range.isEmpty()) {
                range.forEach(value -> {
                    Double score = zSet.score(Constant.RedisArticlePraiseSortKey, value);
                    if (score != null && score > 0) {
                        int index = StringUtils.indexOf(value, splitChar);
                        if (index > 0) {
                            String aId = StringUtils.substring(value, 0, index);
                            String aTitle = StringUtils.substring(value, index + 1);
                            list.add(new ArticlePraiseRankDto(aId, aTitle, score.toString(), score));
                        }
                    }
                });
            }
        }

        map.put("排行榜", list);

        return map;
    }

    // 缓存点赞排行榜
    private void cancelArticlePraiseRank(final PraiseDto dto, Integer total) {
        final String value = dto.getArticleId() + splitChar + dto.getTitle();
        final String key = Constant.RedisArticlePraiseSortKey;

        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
        // 首先清楚之前的数据
        zSet.remove(Constant.RedisArticlePraiseSortKey, value);
        // 往缓存中放入新的值
        zSet.add(key, value, total.doubleValue());
    }

    // 缓存用户点赞过的文章
    private void cancelUserArticlePraise(final PraiseDto dto, Boolean isOn) {
        HashOperations<String, String, String> hash = redisTemplate.opsForHash();
        final String fieldKey = dto.getUserId() + splitChar + dto.getArticleId();
        if (isOn) {
            hash.put(Constant.RedisArticleUserPraiseKey, fieldKey, dto.getTitle());
        } else {
            hash.put(Constant.RedisArticleUserPraiseKey, fieldKey, "");
        }

    }

    public Map<String, Object> getUserArticles(final Integer curUserId) {
        Map<String, Object> map = Maps.newHashMap();
        // 从数据库中直接查询用户详情
        map.put("用户详情", userMapper.selectByPrimaryKey(curUserId));

        // 用户点赞过的历史文章-查redis的hash
        LinkedList<PraiseDto> list = Lists.newLinkedList();
        HashOperations<String, String, String> hash = redisTemplate.opsForHash();
        Map<String, String> stringMap = hash.entries(Constant.RedisArticleUserPraiseKey);
        Set<Map.Entry<String, String>> set = stringMap.entrySet();
        set.forEach(key -> {
            String field = key.getKey();
            String value = key.getValue();
            String[] split = StringUtils.split(field, splitChar);
            if (StringUtils.isNotBlank(value)) {
                if (split[0].equals(curUserId.toString())) {
                    list.add(new PraiseDto(curUserId, Integer.valueOf(split[1]), value));
                }
            }
        });
        map.put("用户点赞过的历史文章", list);
        return map;
    }
}
