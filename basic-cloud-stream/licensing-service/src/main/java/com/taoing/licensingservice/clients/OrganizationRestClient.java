package com.taoing.licensingservice.clients;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.taoing.licensingservice.model.Organization;
import com.taoing.licensingservice.repository.OrgRedisRepository;
import com.taoing.licensingservice.utils.GeneralUtils;
import com.taoing.licensingservice.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 使用 Ribbon-aware Spring RestTemplate 调用服务
 */
@Slf4j
@Component
public class OrganizationRestClient {

    private RestTemplate restTemplate;

    private OrgRedisRepository orgRedisRepository;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    public void setOrgRedisRepository(OrgRedisRepository orgRedisRepository) {
        this.orgRedisRepository = orgRedisRepository;
    }

    private Organization checkRedisCache(Integer orgId) {
        return this.orgRedisRepository.findOrg(orgId);
    }

    private void cacheOrg(Organization org) {
        orgRedisRepository.saveOrg(org);
    }

    /**
     * @HystrixCommand 注解在最外层调用的类方法上才有效
     * @param orgId
     * @return
     */
    @HystrixCommand
    public Organization getOrg(Integer orgId) {
//        GeneralUtils.randomlyRunLong(11000);
        log.info("In licensing service.getOrg: {}", UserContextHolder.getContext().getCorrelationId());
        // 从redis中检查已有缓存
        Organization org = this.checkRedisCache(orgId);

        if (org != null) {
            log.info("I have successfully retrieved an org {} from the redis cache: {}", orgId, org);
            return org;
        }

        log.info("Unable to locate org from the redis cache: {}", orgId);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange("http://zuulservice/api/organization/v1/organizations/{orgId}",
                        HttpMethod.GET,
                        null, Organization.class, orgId);
        org = restExchange.getBody();
        if (org != null) {
            // 存入redis
            this.cacheOrg(org);
        }
        return org;
    }
}
