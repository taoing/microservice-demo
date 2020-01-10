package com.taoing.licensingservice.repository;

import com.taoing.licensingservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

/**
 * @description: redis数据库访问接口实现类
 * @author: mian.tao
 * @date: 2020-01-09 18:28
 */
@Repository
public class OrgRedisRepositoryImpl implements OrgRedisRepository {

    private static final String HASH_NAME = "organization";

    private RedisTemplate<String, Organization> redisTemplate;
    private HashOperations<String, Integer, Organization> hashOperations;

    @Autowired
    public OrgRedisRepositoryImpl(@Qualifier("jsonRedis") RedisTemplate<String, Organization> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        this.hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public void saveOrg(Organization org) {
        this.hashOperations.put(HASH_NAME, org.getId(), org);
    }

    @Override
    public void updateOrg(Organization org) {
        this.hashOperations.put(HASH_NAME, org.getId(), org);
    }

    @Override
    public void deleteOrg(Integer orgId) {
        this.hashOperations.delete(HASH_NAME, orgId);
    }

    @Override
    public Organization findOrg(Integer orgId) {
        return this.hashOperations.get(HASH_NAME, orgId);
    }
}
