package com.xz.seckill.service;

import com.xz.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xz.seckill.vo.LoginVo;
import com.xz.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author xz
* @description 针对表【user】的数据库操作Service
* @createDate 2022-09-26 22:57:59
*/
public interface UserService extends IService<User> {

    /**
     * @param loginVo  11
     * @param request
     * @param response
     * @return 登录
     */
    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 根据 Cookie 获取用户
     * @param ticket
     * @return
     */
    User getUserByCookie(String ticket, HttpServletRequest request, HttpServletResponse response);

    /**
     * 更新密码
     * @return
     */
    RespBean updatePassword(String ticket, HttpServletRequest request, HttpServletResponse response, String newPassword);

}
