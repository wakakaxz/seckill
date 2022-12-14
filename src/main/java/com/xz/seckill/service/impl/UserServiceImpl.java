package com.xz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xz.seckill.exception.GlobalException;
import com.xz.seckill.pojo.User;
import com.xz.seckill.service.UserService;
import com.xz.seckill.mapper.UserMapper;
import com.xz.seckill.util.CookieUtil;
import com.xz.seckill.util.MD5Util;
import com.xz.seckill.util.UUIDUtil;
import com.xz.seckill.vo.LoginVo;
import com.xz.seckill.vo.RespBean;
import com.xz.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;


/**
 * @author xz
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-09-26 22:57:59
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @param loginVo  传入的电话号密码
     * @param request
     * @param response
     * @return 执行结果
     * 可以发现每次都需要大量的判断, 怎么办? 引入 spring-boot-starter-validation
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();


        // 参数校验用自定义注解代替
//        if (!StringUtils.hasText(mobile) || !StringUtils.hasText(password)) {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//
//        if (!ValidatorUtil.isMobile(mobile)) {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }

        // 根据手机号获取用户
        User user = userMapper.selectByUserId(mobile);

        // 是否未注册
        if (null == user) {
            // 用自定义异常处理
//            return RespBean.error(RespBeanEnum.NOT_EXIST_ERROR);
            throw new GlobalException(RespBeanEnum.NOT_EXIST_ERROR);
        }

        // 密码判断
        if (!MD5Util.md5(password).equals(user.getPassword())) {
            // 用自定义异常处理
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }

        // 生成 cookie, 设置 cookie
        String ticket = UUIDUtil.uuid();

        // 弃用该方法, 使用直接 Redis 存储
//        request.getSession().setAttribute(ticket, user);
        redisTemplate.opsForValue().set("user:" + ticket, user);

        // 设置时间
//        redisTemplate.opsForValue().set("user:" + ticket, user, 100, TimeUnit.SECONDS);

        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String ticket, HttpServletRequest request, HttpServletResponse response) {
        if (!StringUtils.hasText(ticket)) {
            return null;
        }

        User user = (User) redisTemplate.opsForValue().get("user:" + ticket);

        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", ticket);
        }

        return user;
    }

    /**
     * 更新密码
     * @param ticket
     * @param request
     * @param response
     * @param newPassword
     * @return
     */
    @Override
    public RespBean updatePassword(String ticket, HttpServletRequest request, HttpServletResponse response, String newPassword) {

        User user = getUserByCookie(ticket, request, response);
        if (user == null) {
            throw new GlobalException(RespBeanEnum.NOT_LOGIN);
        }

        user.setPassword(MD5Util.md5(newPassword));

        int res = userMapper.updatePasswordByUserId(user.getUserId(), user.getPassword());
        if (res == 1) {
            redisTemplate.delete("user:" + ticket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.UPDATE_PASSWORD_FAIL);
    }
}




