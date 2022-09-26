package com.xz.seckill.service;

import com.xz.seckill.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xz.seckill.vo.LoginVo;
import com.xz.seckill.vo.RespBean;

/**
* @author xz
* @description 针对表【user】的数据库操作Service
* @createDate 2022-09-26 22:57:59
*/
public interface UserService extends IService<User> {

    /**
     * @param loginVo  11
     * @return 登录
     */
    RespBean doLogin(LoginVo loginVo);
}
