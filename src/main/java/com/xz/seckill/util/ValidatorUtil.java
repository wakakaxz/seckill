package com.xz.seckill.util;

import java.util.regex.Pattern;

/**
 * @author xz
 * 校验类
 */
public abstract class ValidatorUtil {
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^((13[0-9])|(14(0|[5-7]|9))|(15([0-3]|[5-9]))|(16(2|[5-7]))|(17[0-8])|(18[0-9])|(19([0-3]|[5-9])))\\d{8}$");
    public static boolean isMobile(String mobile) {
        return MOBILE_PATTERN.matcher(mobile).matches();
    }
}
