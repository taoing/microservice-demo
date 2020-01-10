package com.taoing.licensingservice.repository;

import com.taoing.licensingservice.model.Organization;

/**
 * @description: redis数据库访问接口
 * @author: mian.tao
 * @date: 2020-01-09 18:23
 */
public interface OrgRedisRepository {

    void saveOrg(Organization org);

    void updateOrg(Organization org);

    void deleteOrg(Integer orgId);

    Organization findOrg(Integer orgId);
}
