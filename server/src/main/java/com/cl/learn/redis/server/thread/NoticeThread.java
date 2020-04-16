package com.cl.learn.redis.server.thread;/**
 * Created by Administrator on 2020/3/9.
 */

import com.cl.learn.redis.model.entity.Notice;
import com.cl.learn.redis.model.entity.User;
import com.cl.learn.redis.server.service.EmailService;

import java.util.concurrent.Callable;

/**
 * 单一的线程：任务-发送邮件
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/3/9 10:21
 **/
public class NoticeThread implements Callable<Boolean>{

    private EmailService emailService;
    private Notice notice;
    private User user;

    public NoticeThread(EmailService emailService, Notice notice, User user) {
        this.emailService = emailService;
        this.notice = notice;
        this.user = user;
    }

    @Override
    public Boolean call() throws Exception {
        emailService.emailUserNotice(notice,user);
        return true;
    }
}