package com.xz.seckill.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置类
 */
@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queue() {
        // 持久化
        return new Queue("queue", true);
    }

}
