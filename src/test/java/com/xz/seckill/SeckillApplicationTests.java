package com.xz.seckill;

import com.xz.seckill.util.MD5Util;
import com.xz.seckill.util.UUIDUtil;
import com.xz.seckill.vo.RespBeanEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeckillApplicationTests {

    @Test
    void contextLoads() {
    }

    @DisplayName("测试 123456 密码的 md5")
    @Test
    void test() {
        System.out.println(MD5Util.md5("123456"));
    }

    @DisplayName("枚举类型")
    @Test
    void test2() {

        for (RespBeanEnum value : RespBeanEnum.values()) {
            System.out.println(value);
        }
    }

    @DisplayName("uuid")
    @Test
    void test3() {
        System.out.println(UUIDUtil.uuid());
    }

}
