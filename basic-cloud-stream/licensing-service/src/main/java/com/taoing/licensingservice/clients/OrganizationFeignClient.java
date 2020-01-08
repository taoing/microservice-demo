package com.taoing.licensingservice.clients;

import com.taoing.licensingservice.model.Organization;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 使用 Netflix Feign client 调用服务,
 * 抽象服务提供者的接口即可
 */
@FeignClient(value = "organizationservice")
public interface OrganizationFeignClient {
    @RequestMapping(
            value = "/v1/organizations/{orgId}",
            method = RequestMethod.GET
    )
    Organization getOrg(@PathVariable("orgId") Integer orgId);
}
