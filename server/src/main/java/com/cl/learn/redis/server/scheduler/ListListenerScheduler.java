package com.cl.learn.redis.server.scheduler;/**
 * Created by Administrator on 2020/3/9.
 */


import com.cl.learn.redis.model.entity.Notice;
import com.cl.learn.redis.model.entity.User;
import com.cl.learn.redis.model.mapper.UserMapper;
import com.cl.learn.redis.server.enums.Constant;
import com.cl.learn.redis.server.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 列表list的监听任务调度器
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2020/3/9 9:54
 **/
@Component
public class ListListenerScheduler {

    private static final Logger log= LoggerFactory.getLogger(ListListenerScheduler.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserMapper userMapper;


    //TODO:近实时的监听列表list中的消息
    @Scheduled(cron = "0/2 * * * * ?")
    public void listenNotice(){
        log.debug("--近实时的监听列表list中的消息--");

        ListOperations<String, Notice> listOperations=redisTemplate.opsForList();
        Notice notice=listOperations.rightPop(Constant.RedisListNoticeKey);


        /*//问题的根源：while没有终止条件-才导致一直在那里不间断的执行...
        while (notice!=null){
            //TODO:发送这个通知公告的消息给到指定的商户
            this.sendNoticeToUser(notice);
        }*/


        while (notice!=null){
            //TODO:发送这个通知公告的消息给到指定的商户
            this.sendNoticeToUser(notice);

            notice=listOperations.rightPop(Constant.RedisListNoticeKey);
        }
    }

    private void sendNoticeToUser(Notice notice){
        if (notice!=null){
            //方式一
            List<User> list=userMapper.selectList();
            if (list!=null && !list.isEmpty()){
                //list.forEach(user -> emailService.sendSimpleEmail(notice.getTitle(),notice.getContent(),user.getEmail()));
                list.forEach(user -> emailService.sendSimpleEmail(notice.getTitle(),notice.getContent(),user.getEmail()));
            }

            //方式二：多线程
            /*try {
                ExecutorService executorService=Executors.newFixedThreadPool(10);
                List<NoticeThread> threads= Lists.newLinkedList();

                List<User> list=userMapper.selectList();
                if (list!=null && !list.isEmpty()){
                    list.forEach(user -> threads.add(new NoticeThread(emailService,notice,user)));
                }

                executorService.invokeAll(threads);
            }catch (Exception e){
                log.error("--近实时的监听列表list中的消息-多线程发送通知公告-发生异常：",e);
            }*/
        }

    }
}



























