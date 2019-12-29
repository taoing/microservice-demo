package com.taoing.licensingservice.clients;

import com.taoing.licensingservice.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 使用 Ribbon-aware Spring RestTemplate 调用服务
 */
@Component
public class OrganizationRestTemplateClient {

    private RestTemplate restTemplate;

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Organization getOrg(Integer orgId) {
        ResponseEntity<Organization> restExchange =
                restTemplate.exchange("http://organizationservice/v1/organizations/{orgId}",
                        HttpMethod.GET,
                        null, Organization.class, orgId);
        return restExchange.getBody();
    }
}
