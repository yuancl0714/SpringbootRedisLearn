package com.cl.learn.redis.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author chenLei
 * @date 2020/8/28 23:54
 */
@RequestMapping(value = "你请求的这个controller的总路径")
public class DemoController {
    /**
     * 客户跟进记录
     * @param 参数名 你从前端接收的参数封装的实体类
     */
    @RequestMapping(value = "这个请求的路径")
        public void customInfo(接受参数实体类 name){
        // 调用service层中的方法
        List<实体类> list = service.方法名();
        // 设置导出的信息，将list导出
    }
    /**
     * service层操作
     * service类中定义方法接口
     * serviceImpl中实现service类中的接口，并写方法体
     */
    /**
     * 数据库操作
     * select * from 表名
     * <where>
     *     <if test="项目名称 ！= '' && 项目名称 ！= null">
     *         and 项目名称 = {你实体类里面的项目名称字段}
     *     </if>
     *     <if test="维护人 ！= '' && 维护人 ！= null">
     *          and 维护人 = {你实体类中的维护人字段}
     *     </if>
     *     以此类推......
     * </where>
     */
}
