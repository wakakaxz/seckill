package com.xz.seckill.util;

import org.springframework.util.DigestUtils;

/**
 * @author xz
 * MD5 加密
 */
public abstract class MD5Util {
    private static final String SALT_PRE = "qwe";
    private static final String SALT_SUF = "123";

    public static String md5(String password) {
        return DigestUtils.md5DigestAsHex((SALT_PRE + password + SALT_SUF).getBytes());
    }
}
