package com.taoing.licensingservice.clients;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.taoing.licensingservice.model.Organization;
import com.taoing.licensingservice.utils.GeneralUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 使用 Ribbon-aware Spring RestTemplate 调用服务
 */
@Component
public class OrganizationRestClient {

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * @param orgId
     * @return
     * @HystrixCommand 注解在最外层调用的类方法上才有效
     */
    @HystrixCommand
    public Organization getOrg(Integer orgId) {
        GeneralUtils.randomlyRunLong(11000);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange("http://organizationservice/v1/organizations/{orgId}",
                        HttpMethod.GET,
                        null, Organization.class, orgId);
        return restExchange.getBody();
    }
}
