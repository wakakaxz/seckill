package com.xz.seckill.vo;

import com.xz.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author xz
 * 登录 VO
 */
@Data
public class LoginVo {
    /**
     * 需要自定义捕获异常, 不然的话会直接把全部的错误信息一下子全返回给界面
     * 两种方式,
     * ControllerAdvance 只能捕获控制器异常, 定义多个拦截方法, 抛出异常信息
     * ErrorHandler 处理所有异常, 404 等
     * */
    @NotNull
    @IsMobile
    private String mobile;


    @NotNull
    @Length(min = 6)
    private String password;
}
