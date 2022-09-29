package com.xz.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author xz
 * 公共返回对象枚举
 */

@AllArgsConstructor
@ToString
@Getter
public enum RespBeanEnum {
    /**
     * 通用状态码
     */
    SUCCESS(200, "SUCCESS"),
    ERROR(500, "服务端异常,"),

    /**
     * 登录模块
     */
    LOGIN_ERROR(500210, "输入的用户名或密码错误"),
    MOBILE_ERROR(500211, "输入的手机号码不正确"),
    NOT_EXIST_ERROR(500212, "用户不存在"),
    BIND_ERROR(500213, "参数校验异常"),
    NOT_LOGIN(500214, "未获取到用户的登录信息"),

    /**
     * 秒杀模块
     * */
    EMPTY_STOCK(500500, "库存不足"),
    REPEAT_ERROR(500501, "重复购买, 只限购买一件");

    private final Integer code;
    private final String message;
}
