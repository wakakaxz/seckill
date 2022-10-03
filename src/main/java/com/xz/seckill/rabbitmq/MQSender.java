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

    /**
     * 发送秒杀信息
     * @param message
     */
    public void sendSeckillMessage(String message) {
        log.info("发送消息: " + message);
        rabbitTemplate.convertAndSend("seckillExchange", "seckill.message", message);
    }

//    public void send(Object msg) {
//        log.info("发送消息: " + msg);
//        rabbitTemplate.convertAndSend("queue", msg);
//    }

//    public void send(Object msg) {
//        log.info("发送消息: " + msg);
//        // 发送到交换机
//        rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
//    }
//
//    public void sendRoutingRed(Object msg) {
//        log.info("发送 red 消息: " + msg);
//        rabbitTemplate.convertAndSend("directExchange", "queue.red", msg);
//    }
//
//    public void sendRoutingGreen(Object msg) {
//        log.info("发送 green 消息: " + msg);
//        rabbitTemplate.convertAndSend("directExchange", "queue.green", msg);
//    }
//
//    public void sendTopic01(Object msg) {
//        log.info("发送给 #.queue.# 消息: " + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "queue.red.message", msg);
//    }
//
//    public void sendTopic02(Object msg) {
//        log.info("发送给两个 queue 的消息: " + msg);
//        rabbitTemplate.convertAndSend("topicExchange", "message.queue.green.test", msg);
//    }

}
