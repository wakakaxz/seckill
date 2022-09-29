package com.xz.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xz.seckill.pojo.Order;
import com.xz.seckill.pojo.SeckillOrder;
import com.xz.seckill.pojo.User;
import com.xz.seckill.service.GoodsService;
import com.xz.seckill.service.OrderService;
import com.xz.seckill.service.SeckillOrderService;
import com.xz.seckill.vo.GoodsVo;
import com.xz.seckill.vo.RespBean;
import com.xz.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillOrderService seckillOrderService;

    @Autowired
    private OrderService orderService;

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

        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        // 判断库存
        if (goodsVo.getStockCount() < 1) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        // 判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().
                eq("user_id", user.getUserId()).eq("goods_id", goodsId));
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }

        Order order = orderService.seckill(user, goodsVo);

        Map<String, Object> map = new HashMap<>();
        map.put("order", order);
        map.put("goods", goodsVo);
        return RespBean.success(map);
    }

}
