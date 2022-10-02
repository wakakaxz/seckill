package com.xz.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 消息发送者
 *
 * @author xz
 */
@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void send(Object msg) {
//        log.info("发送消息: " + msg);
//        rabbitTemplate.convertAndSend("queue", msg);
//    }

    public void send(Object msg) {
        log.info("发送消息: " + msg);
        // 发送到交换机
        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
    }

    public void sendRoutingRed(Object msg) {
        log.info("发送 red 消息: " + msg);
        rabbitTemplate.convertAndSend("directExchange", "queue.red", msg);
    }

    public void sendRoutingGreen(Object msg) {
        log.info("发送 green 消息: " + msg);
        rabbitTemplate.convertAndSend("directExchange", "queue.green", msg);
    }
}
