package com.cl.learn.redis.server.service;

import com.cl.learn.redis.model.entity.Notice;
import com.cl.learn.redis.model.mapper.NoticeMapper;
import com.cl.learn.redis.server.controller.AbstractController;
import com.cl.learn.redis.server.enums.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author chenLei
 * @date 2020/4/12 11:45
 */
@Service
public class NoticeService extends AbstractController {

    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    // 平台管理员添加管理公告
    public Integer addNotice(Notice notice) {

        // 添加到数据库中
        int selective = noticeMapper.insertSelective(notice);
        if (selective > 0) {
            // 往缓存中添加一份
            ListOperations<String, Notice> list = redisTemplate.opsForList();
            list.leftPush(Constant.RedisListNoticeKey + notice.getId(), notice);
        }
        return 1;
    }

}
