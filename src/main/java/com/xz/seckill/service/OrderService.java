package com.xz.seckill.service;

import com.xz.seckill.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xz.seckill.pojo.User;
import com.xz.seckill.vo.GoodsVo;

/**
* @author xz
* @description 针对表【order】的数据库操作Service
* @createDate 2022-09-28 14:19:25
*/
public interface OrderService extends IService<Order> {

    Order seckill(User user, GoodsVo goodsVo);

    Order detailById(Long orderId);
}
