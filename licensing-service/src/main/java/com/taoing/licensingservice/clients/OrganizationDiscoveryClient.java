package com.taoing.licensingservice.clients;

import com.taoing.licensingservice.model.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 使用 Spring DiscoveryClient 查找服务实例
 */
@Component
@Slf4j
public class OrganizationDiscoveryClient {

    private DiscoveryClient discoveryClient;

    @Autowired
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public Organization getOrg(Integer orgId) {
        RestTemplate restTemplate = new RestTemplate();
        // 服务id查找服务列表
        List<ServiceInstance> instances = discoveryClient.getInstances("organizationservice");
        if (instances.size() == 0) {
            return null;
        }
        String serviceUri = String.format("%s/v1/organizations/%s", instances.get(0).getUri().toString(), orgId);
        log.info("!!! SERVICE URI: " + serviceUri);

        ResponseEntity<Organization> restExchange =
                restTemplate.exchange(serviceUri,
                        HttpMethod.GET,
                        null, Organization.class, orgId);
        return restExchange.getBody();
    }
}
