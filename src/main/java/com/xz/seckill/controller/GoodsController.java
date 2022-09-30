package com.xz.seckill.controller;

import com.xz.seckill.pojo.User;
import com.xz.seckill.service.GoodsService;
import com.xz.seckill.service.UserService;
import com.xz.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UserService userService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 加上页面缓存 consumes=application/json
     * 参数表示（方法仅处理request Content-Type为“application/json”类型的请求。）
     * @param user
     * @return
     */
    @GetMapping("/toList")
    public List<GoodsVo> toList(User user) {
        // 先从 Redis 中取缓存, 若返回的对象不为空则直接返回对象
        ValueOperations valueOperations = redisTemplate.opsForValue();
        List<GoodsVo> goodList = (List<GoodsVo>) valueOperations.get("goodsList");
        if (!ObjectUtils.isEmpty(goodList)) {
            return  goodList;
        }

        // 若 Redis 中没有则从数据库加载
        List<GoodsVo> list = goodsService.findGoodVo();
        // 设置失效时间 60 s
        valueOperations.set("goodsList", list, 60, TimeUnit.SECONDS);

        return list;

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

//        return goodsService.findGoodVo();
    }

    @GetMapping("/toDetail/{goodsId}")
    public GoodsVo toDetail(User user, @PathVariable Long goodsId) {
        // 同样加上页面缓存
        ValueOperations valueOperations = redisTemplate.opsForValue();
        GoodsVo goodsVo = (GoodsVo) valueOperations.get("goodsDetails:" + goodsId);
        if (!ObjectUtils.isEmpty(goodsVo)) {
            return goodsVo;
        }

        // 设置 60 s 过期时间, 这个按情况说, 秒杀场景商品可以设置小一些
        GoodsVo goodVoByGoodsId = goodsService.findGoodVoByGoodsId(goodsId);
        valueOperations.set("goodsDetails:" + goodsId, goodVoByGoodsId, 60, TimeUnit.SECONDS);
        return goodVoByGoodsId;

//        return goodsService.findGoodVoByGoodsId(goodsId);
    }

}
