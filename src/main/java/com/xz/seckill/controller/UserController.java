package com.xz.seckill.controller;


import com.xz.seckill.pojo.User;
import com.xz.seckill.rabbitmq.MQSender;
import com.xz.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private MQSender mqSender;

    /**
     * 获取用户信息
     * @param user
     * @return
     */
    @GetMapping("/info")
    public RespBean info (User user) {
        return RespBean.success(user);
    }

    /**
     * 测试 发送 RabbitMQ 消息
     */
    @GetMapping("/mq")
    public void mq() {
        mqSender.send("Hello, 哒哒哒");
    }

    /**
     * fanout 模式测试, 向交换机发送一条消息
     */
    @GetMapping("/mq/fanout")
    public void mqFanout() {
        mqSender.send("Hello, 发布者发送一条消息");
    }
}
