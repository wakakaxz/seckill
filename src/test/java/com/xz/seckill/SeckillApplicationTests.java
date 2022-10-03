package com.xz.seckill;

import com.xz.seckill.util.MD5Util;
import com.xz.seckill.util.UUIDUtil;
import com.xz.seckill.util.UserUtil;
import com.xz.seckill.vo.RespBeanEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class SeckillApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisScript<Boolean> script;


    @DisplayName("测试 redis 锁")
    @Test
    void testRedisLock01() {
        // 当 key 不存在的时候才能设置成功
        Boolean isLock = redisTemplate.opsForValue().setIfAbsent("k1", "v1", 5, TimeUnit.SECONDS);
        // 上锁成功, 执行代码, 结束时删除 key
        if (isLock) {
            redisTemplate.opsForValue().set("name", "往里拐");
            String name = (String) redisTemplate.opsForValue().get("name");
            System.out.println("name = " + name);

            /**
             *  假设这里抛出异常, 那么这个锁永远无法释放,
             *  解决方法 1: 可以在分布式锁上定一个时间(假设为 5s), 但是定时间会发生不可预知的问题,
             *      比如, 第一个线程需要执行 7s , 第 5s 的时候锁就自动释放了, 第二个线程进来
             *      过了 2s, 第一个线程执行结束并释放第二个线程的锁, 又过 3s, 第二个线程结束并释放下一个线程的锁..., 引起混乱
             *
             *  解决方法 2:
             *      上锁的时候: 设置 key : 随机值
             *      解锁的时候: 判断有没有 key, 有的话看 value 是不是一开始上锁的时候设置的随机值, 若一样则释放, 不一致不释放
             *      因为不是原子操作, 可以设置一个 lua 脚本来执行
             * */


            redisTemplate.delete("k1");
        } else {
            System.out.println("有线程在使用, 请稍后再试!");
        }
    }

    @DisplayName("测试 redis Lua 脚本")
    @Test
    void testRedisLock02() {
        // 取得随机值
        String value = UUIDUtil.uuid();
        // 当 key 不存在的时候才能设置成功
        Boolean isLock = redisTemplate.opsForValue().setIfAbsent("k1", value, 20, TimeUnit.SECONDS);
        // 上锁成功, 执行代码, 结束时删除 key
        if (isLock) {
            redisTemplate.opsForValue().set("name", "往里拐");
            String name = (String) redisTemplate.opsForValue().get("name");
            System.out.println("name = " + name);

            System.out.println("即将删除的锁 value: " +  redisTemplate.opsForValue().get("k1"));

//            redisTemplate.delete("k1");
            Boolean result = (Boolean) redisTemplate.execute(script, Collections.singletonList("k1"), value);
            System.out.println(result);
        } else {
            System.out.println("有线程在使用, 请稍后再试!");
        }
    }

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

    @DisplayName("用户生成")
    @Test
    void test4() {
//        try {
//            UserUtil.createUser(10);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }
}
