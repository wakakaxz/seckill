package com.xz.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xz.seckill.pojo.User;
import com.xz.seckill.service.UserService;
import com.xz.seckill.mapper.UserMapper;
import com.xz.seckill.util.MD5Util;
import com.xz.seckill.util.ValidatorUtil;
import com.xz.seckill.vo.LoginVo;
import com.xz.seckill.vo.RespBean;
import com.xz.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


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

    @Override
    public RespBean doLogin(LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        if (!StringUtils.hasText(mobile) || !StringUtils.hasText(password)) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        if (!ValidatorUtil.isMobile(mobile)) {
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }

        // 根据手机号获取用户
        User user = userMapper.selectByUserId(mobile);

        // 是否未注册
        if (null == user) {
            return RespBean.error(RespBeanEnum.NOT_EXIST_ERROR);
        }

        // 密码判断
        if (!MD5Util.md5(mobile).equals(user.getPassword())) {
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }

        return RespBean.success();
    }
}




