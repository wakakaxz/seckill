package com.xz.seckill.controller;


import com.xz.seckill.pojo.SeckillMessage;
import com.xz.seckill.pojo.SeckillOrder;
import com.xz.seckill.pojo.User;
import com.xz.seckill.rabbitmq.MQSender;
import com.xz.seckill.service.GoodsService;
import com.xz.seckill.service.OrderService;
import com.xz.seckill.service.SeckillOrderService;
import com.xz.seckill.util.JsonUtil;
import com.xz.seckill.vo.GoodsVo;
import com.xz.seckill.vo.RespBean;
import com.xz.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MQSender mqSender;

    @Autowired
    private RedisScript<Long> redisScript;

    /**
     * 标记库存为空
     */
    private Map<Long, Boolean> emptyStockMap = new HashMap<>();

    /**
     * 加上 @RequestParam 注解可以防止传来的参数为空, 若为空抛出异常, 自定义校验直接使用 @Valid
     *
     * @param user
     * @param goodsId
     * @return
     */
    @PostMapping("/doSeckill")
    public RespBean doSeckill(User user, @RequestParam Long goodsId) {
        if (null == user) {
            return RespBean.error(RespBeanEnum.NOT_LOGIN);
        }
        /*
        * GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        // 判断库存
        if (goodsVo.getStockCount() < 1) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 判断是否重复抢购
//        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().
//                eq("user_id", user.getUserId()).eq("goods_id", goodsId));

        // 从 Redis 中获取
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getUserId() + ":" + goodsId);

        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        Order order = orderService.seckill(user, goodsVo);

        Map<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("goods", goodsVo);
        return RespBean.success(map);
        * */


        /*
          Redis 预减库存, 订单入队, 客户端轮询访问是否下单成功
          */
        // 判断是否重复抢购
        SeckillOrder seckillOrder = (SeckillOrder) redisTemplate.opsForValue().get("order:" + user.getUserId() + ":" + goodsId);
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        // 内存标记, 减少 Redis 的访问次数
        if (emptyStockMap.get(goodsId)) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 预减库存, 返回一个递减后的库存
//        Long stock = redisTemplate.opsForValue().decrement("seckillGoods:" + goodsId);

        Long stock = (Long) redisTemplate.execute(redisScript,
                Collections.singletonList("seckillGoods:" + goodsId),
                Collections.EMPTY_LIST);

        if (stock < 0) {
            emptyStockMap.put(goodsId, true);
            // 防止库存数量为 -1, 递增一下
            redisTemplate.opsForValue().increment("seckillGoods:" + goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 下单
        SeckillMessage seckillMessage = new SeckillMessage(user, goodsId);
        // 发送消息
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));

        // 0 表示排队中
        return RespBean.success(0);
    }

    /**
     * 获取描述结果
     *
     * @param user
     * @param goodsId
     * @return orderId 成功, -1: 秒杀失败,  0: 排队中
     */
    @GetMapping("/result")
    public RespBean getResult(User user, @RequestParam Long goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.NOT_LOGIN);
        }
        Long orderId = seckillOrderService.getResult(user, goodsId);
        return RespBean.success(orderId);
    }


    /**
     * Bean 初始化时执行的方法
     * 1：spring为bean提供了两种初始化bean的方式，实现InitializingBean接口，实现afterPropertiesSet方法，
     * 或者在配置文件中同过init-method指定，两种方式可以同时使用
     * 2：实现InitializingBean接口是直接调用afterPropertiesSet方法，比通过反射调用init-method指定的方法效率相对来说要高点。
     * 但是init-method方式消除了对spring的依赖
     * 3：如果调用afterPropertiesSet方法时出错，则不调用init-method指定的方法。
     *
     * @throws Exception 系统初始化的时候将商品的库存数量加载到 Redis
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodVo();
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:" + goodsVo.getId(), goodsVo.getStockCount());
            emptyStockMap.put(goodsVo.getId(), false);
        });
    }
}
