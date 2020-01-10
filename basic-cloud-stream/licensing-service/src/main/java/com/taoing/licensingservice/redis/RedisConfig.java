package com.taoing.licensingservice.redis;

import com.taoing.licensingservice.model.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Organization> jdkRedis(RedisConnectionFactory cf) {
        RedisTemplate<String, Organization> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // JdkSerializationRedisSerializer为默认的value序列化机制
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Organization> jsonRedis(RedisConnectionFactory cf) {
        RedisTemplate<String, Organization> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(cf);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Organization.class));
        // 额外为hash里的key, value设置序列化器
        redisTemplate.setHashKeySerializer(new Jackson2JsonRedisSerializer<>(Integer.class));
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Organization.class));
        return redisTemplate;
    }
}
