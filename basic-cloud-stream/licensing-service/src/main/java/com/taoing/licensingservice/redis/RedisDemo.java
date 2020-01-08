package com.taoing.licensingservice.redis;

import com.taoing.licensingservice.model.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RedisDemo {

    @Autowired
    private RedisTemplate<String, Organization> redisTemplate;

    @Autowired
    private RedisConnectionFactory cf;

    public String serializeDemo() {
        Organization org = new Organization();
        org.setId(100);
        org.setName("customer-crm-co");
        org.setContactName("Mark Balster");
        org.setContactEmail("mark.balster@custcrmco.com");
        org.setContactPhone("823-555-1212");

        // java对象序列化
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOut = new ObjectOutputStream(buffer);
            objectOut.writeObject(org);
            objectOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = buffer.toByteArray();
        String jdkSerializeString = new String(bytes, StandardCharsets.UTF_8);
        log.info("jdk serialize String: {}", jdkSerializeString);

        // 对象存入redis
        // 使用java序列化机制的value序列化器, value序列后存入redis的string与jdkSerializeString一致
        String key = "org";
        redisTemplate.opsForValue().set(key, org);

        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(cf);
        stringRedisTemplate.afterPropertiesSet();

        // 从redis读取序列化后的字符串
        String stringFromRedis = stringRedisTemplate.opsForValue().get(key);
        if (jdkSerializeString.equals(stringFromRedis)) {
            log.info("使用java序列化机制的value序列化器, value序列后存入redis的string与jdkSerializeString一致");
        }
        return stringFromRedis;
    }
}
