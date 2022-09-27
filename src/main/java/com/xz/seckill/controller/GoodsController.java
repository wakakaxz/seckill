package com.xz.seckill.controller;

import com.xz.seckill.pojo.User;
import com.xz.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private UserService userService;

    @GetMapping("/toList")
    public User toList(HttpServletRequest request, HttpServletResponse response, @CookieValue("userTicket") String ticket) {

        if (!StringUtils.hasText(ticket)) {
            return null;
        }

        // 弃用该方法, 直接在 redis 中获取
//        User user = (User) session.getAttribute(ticket);

        User user = userService.getUserByCookie(ticket, request, response);

        if (null == user) {
            return null;
        }

        return user;
    }

}
