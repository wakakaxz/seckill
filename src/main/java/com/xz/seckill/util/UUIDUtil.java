package com.xz.seckill.util;

import java.util.UUID;

/**
 * UUID 工具类, 可以用雪花算法代替
 *
 * @author xz
 */
public abstract class UUIDUtil {

    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
