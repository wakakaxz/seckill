package com.xz.seckill.controller;

import com.xz.seckill.pojo.User;
import com.xz.seckill.service.GoodsService;
import com.xz.seckill.service.UserService;
import com.xz.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/toList")
    public List<GoodsVo> toList(User user) {

//        if (!StringUtils.hasText(ticket)) {
//            return null;
//        }

        // 弃用该方法, 直接在 redis 中获取
//        User user = (User) session.getAttribute(ticket);

//        User user = userService.getUserByCookie(ticket, request, response);
//
//        if (null == user) {
//            return null;
//        }
        // 直接判断是否登录即可
        return goodsService.findGoodVo();
    }

    @GetMapping("/toDetail/{goodsId}")
    public GoodsVo toDetail(User user, @PathVariable Long goodsId) {
        System.out.println(user);
        return goodsService.findGoodVoByGoodsId(goodsId);
    }

}
