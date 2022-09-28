package com.xz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xz.seckill.pojo.Order;
import com.xz.seckill.service.OrderService;
import com.xz.seckill.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author xz
* @description 针对表【order】的数据库操作Service实现
* @createDate 2022-09-28 14:19:25
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




