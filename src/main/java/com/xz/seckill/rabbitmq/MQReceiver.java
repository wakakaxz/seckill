package com.xz.seckill.rabbitmq;

import com.xz.seckill.pojo.Order;
import com.xz.seckill.pojo.SeckillMessage;
import com.xz.seckill.pojo.SeckillOrder;
import com.xz.seckill.pojo.User;
import com.xz.seckill.service.GoodsService;
import com.xz.seckill.service.OrderService;
import com.xz.seckill.util.JsonUtil;
import com.xz.seckill.vo.GoodsVo;
import com.xz.seckill.vo.RespBean;
import com.xz.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息接收者
 * @author xz
 */
@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private OrderService orderService;

    /**
     * 接收消息, 下单操作
     * @param message
     */
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message) {
        log.info("接受到的消息: " + message);
        SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(message, SeckillMessage.class);
        Long goodsId = seckillMessage.getGoodsId();
        User user = seckillMessage.getUser();
        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        if (goodsVo.getStockCount() < 1) {
            return;
        }

        // 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getUserId() + ":" + goodsId);
        if (seckillOrder != null) {
            return;
        }

        // 秒杀
        orderService.seckill(user, goodsVo);
    }

//    @RabbitListener(queues = "queue")
//    public void receive(Object msg) {
//        log.info("接收消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg) {
//        log.info("QUEUE01接收消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg) {
//        log.info("QUEUE02接收消息: " + msg);
//    }
//
//
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg) {
//        log.info("QUEUERed接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg) {
//        log.info("QUEUEGreen接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg) {
//        log.info("#.queue.# 接受消息: " + msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void receive06(Object msg) {
//        log.info("*.queue.# 接受消息: " + msg);
//    }
}
