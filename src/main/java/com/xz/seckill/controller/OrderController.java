package com.xz.seckill.controller;

import com.xz.seckill.pojo.Order;
import com.xz.seckill.pojo.User;
import com.xz.seckill.service.OrderService;
import com.xz.seckill.vo.RespBean;
import com.xz.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/detail")
    public RespBean detail(User user, Long orderId) {
        if (user == null) {
            return  RespBean.error(RespBeanEnum.NOT_LOGIN);
        }

        Order order = orderService.detailById(orderId);
        return RespBean.success();
    }

}
