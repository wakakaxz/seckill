package com.xz.seckill.controller;


import com.xz.seckill.service.UserService;
import com.xz.seckill.vo.LoginVo;
import com.xz.seckill.vo.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/doLogin")
    public RespBean doLogin(LoginVo loginVo) {
        return userService.doLogin(loginVo);
    }

}