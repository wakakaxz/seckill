package com.xz.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xz.seckill.exception.GlobalException;
import com.xz.seckill.mapper.SeckillGoodsMapper;
import com.xz.seckill.mapper.SeckillOrderMapper;
import com.xz.seckill.pojo.Order;
import com.xz.seckill.pojo.SeckillGoods;
import com.xz.seckill.pojo.SeckillOrder;
import com.xz.seckill.pojo.User;
import com.xz.seckill.service.OrderService;
import com.xz.seckill.mapper.OrderMapper;
import com.xz.seckill.vo.GoodsVo;
import com.xz.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author xz
 * @description 针对表【order】的数据库操作Service实现
 * @createDate 2022-09-28 14:19:25
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
        implements OrderService {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional // 事务注解
    @Override
    public Order seckill(User user, GoodsVo goodsVo) {
        // 减库存
        SeckillGoods seckillGoods = seckillGoodsMapper.selectOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsVo.getId()));
        if (seckillGoods.getStockCount() < 1) {
            // 没库存
            redisTemplate.opsForValue().set("isStockEmpty:" + goodsVo.getId(), "0");
            throw new GlobalException(RespBeanEnum.EMPTY_STOCK);
        }
//        seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);

        /**
         * 优化超卖问题
         * 在数据库中将 seckill_order 表中 user_id 字段和 goods_id 字段组成联合唯一索引
         * */
//        seckillGoodsMapper.updateById(seckillGoods);
        int seckillResult = seckillGoodsMapper.update(null, new UpdateWrapper<SeckillGoods>()
                .setSql("stock_count = stock_count - 1")
                .eq("id", seckillGoods.getId())
                .gt("stock_count", 0));

        // 是否插入成功
        if (seckillResult == 0) {
            throw new GlobalException(RespBeanEnum.EMPTY_STOCK);
        }

        // 生成订单
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setGoodsId(goodsVo.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        // 生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setUserId(user.getUserId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrderMapper.insert(seckillOrder);

        // 秒杀订单存储在 Redis
        redisTemplate.opsForValue().set("order:" + user.getUserId() + ":" + goodsVo.getId(),
                seckillOrder, 10, TimeUnit.MINUTES);

        return order;
    }

    @Override
    public Order detailById(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }

        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        return order;
    }
}




