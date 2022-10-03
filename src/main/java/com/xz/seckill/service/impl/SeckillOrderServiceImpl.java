package com.xz.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xz.seckill.pojo.SeckillOrder;
import com.xz.seckill.pojo.User;
import com.xz.seckill.service.SeckillOrderService;
import com.xz.seckill.mapper.SeckillOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
* @author xz
* @description 针对表【seckill_order】的数据库操作Service实现
* @createDate 2022-09-28 14:19:25
*/
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder>
    implements SeckillOrderService{

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 获取秒杀结果
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public Long getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder = seckillOrderMapper.selectOne(new QueryWrapper<SeckillOrder>()
                .eq("user_id", user.getUserId())
                .eq("goods_id", goodsId));
        // 已经生成订单, 返回订单号
        if (seckillOrder != null) {
            return seckillOrder.getOrderId();
        }

        // 若没库存
        if (redisTemplate.hasKey("isStockEmpty:" + goodsId)) {
            return -1L;
        }
        // 排队中
        return 0L;
    }
}




