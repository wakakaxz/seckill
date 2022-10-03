package com.xz.seckill.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    /**
     * 设置redis键值的序列化方式
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        /*
         * value值的序列化可采用如下几种方式
         * 1.默认采用JdkSerializationRedisSerializer 序列化后的长度最短，时间适中，但不是明文显示
         * 2.采用Jackson2JsonRedisSerializer 明文显示，序列化速度最快，长度适中，但会使存入redis中timestamp类型的数据以long类型存储
         * 3.采用GenericJackson2JsonRedisSerializer 明文显示，在redis中显示了@class字段保存有类型的包路径，反序列化更容易，但是序列化时间最长，长度最大，明文显示
         * 4.自定义FastJsonRedisSerializer实现RedisSerializer接口
         *
         * 这里使用Jackson2JsonRedisSerializer，并对日期类型做特别处理
         */

        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper om = new ObjectMapper();
        // 指定要序列化的域，field,get和set,以及修饰符范围，ANY是都有包括private和public
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        /*
         *  指定序列化输入的类型，类必须是非final修饰的，final修饰的类，比如String,Integer等会跑出异常
         *  过期方法：om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
         */
        om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);

        // 日期序列化处理
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        om.registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .registerModule(new ParameterNamesModule());
        redisSerializer.setObjectMapper(om);

        // 设置值（value）的序列化采用Jackson2JsonRedisSerializer
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);

        // 设置键（key）的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        template.afterPropertiesSet();

        return template;
    }

//    @Bean
//    public RedisScript<Boolean> script() {
//        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
//        // lock.lua 脚本位置
//        redisScript.setLocation(new ClassPathResource("redisLua/lock.lua"));
//        redisScript.setResultType(Boolean.class);
//        return redisScript;
//    }

    @Bean
    public RedisScript<Long> script() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // lock.lua 脚本位置
        redisScript.setLocation(new ClassPathResource("redisLua/stock.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}