package com.xz.seckill.controller;


import com.xz.seckill.pojo.User;
import com.xz.seckill.vo.RespBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取用户信息
     * @param user
     * @return
     */
    @GetMapping("/info")
    public RespBean info (User user) {
        return RespBean.success(user);
    }

}
