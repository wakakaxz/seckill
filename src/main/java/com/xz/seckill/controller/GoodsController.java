package com.xz.seckill.controller;

import com.xz.seckill.pojo.User;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @GetMapping("/toList")
    public User toList(HttpSession session,  @CookieValue("userTicket") String ticket) {

        if (!StringUtils.hasText(ticket)) {
            return null;
        }

        User user = (User) session.getAttribute(ticket);

        if (null == user) {
            return null;
        }

        return user;
    }

}
