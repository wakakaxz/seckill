package com.xz.seckill.service;

import com.xz.seckill.pojo.SeckillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xz.seckill.pojo.User;

/**
* @author xz
* @description 针对表【seckill_order】的数据库操作Service
* @createDate 2022-09-28 14:19:25
*/
public interface SeckillOrderService extends IService<SeckillOrder> {

    Long getResult(User user, Long goodsId);
}
