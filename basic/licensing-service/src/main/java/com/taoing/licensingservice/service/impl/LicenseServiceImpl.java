package com.taoing.licensingservice.service.impl;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.taoing.licensingservice.clients.OrganizationDiscoveryClient;
import com.taoing.licensingservice.clients.OrganizationFeignClient;
import com.taoing.licensingservice.clients.OrganizationRestClient;
import com.taoing.licensingservice.config.ServiceConfig;
import com.taoing.licensingservice.mapper.LicenseMapper;
import com.taoing.licensingservice.model.License;
import com.taoing.licensingservice.model.Organization;
import com.taoing.licensingservice.service.LicenseService;
import com.taoing.licensingservice.utils.GeneralUtils;
import com.taoing.licensingservice.utils.UserContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LicenseServiceImpl implements LicenseService {

    private LicenseMapper mapper;

    private OrganizationDiscoveryClient organizationDiscoveryClient;

    private OrganizationRestClient organizationRestClient;

    private OrganizationFeignClient organizationFeignClient;

    private ServiceConfig config;

    @Autowired
    public void setMapper(LicenseMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setOrganizationDiscoveryClient(OrganizationDiscoveryClient organizationDiscoveryClient) {
        this.organizationDiscoveryClient = organizationDiscoveryClient;
    }

    @Autowired
    public void setOrganizationRestClient(OrganizationRestClient organizationRestClient) {
        this.organizationRestClient = organizationRestClient;
    }

    @Autowired
    public void setOrganizationFeignClient(OrganizationFeignClient organizationFeignClient) {
        this.organizationFeignClient = organizationFeignClient;
    }

    @Autowired
    public void setConfig(ServiceConfig config) {
        this.config = config;
    }

    /**
     * @HystrixCommand注解被用于使用 Hystrix 断路器包装方法, 注解在最外层调用的类方法上才有效
     * commandProperties 属性让你提供额外的属性来自定义 Hystrix,
     * execution.isolation.thread.timeoutInMilliseconds 用于设置断路器的超时时间长度（以毫秒为单位）
     * @param organizationId
     * @return
     */
    @Override
    @HystrixCommand(
            fallbackMethod = "buildFallbackLicenseList",
            threadPoolKey = "licensesByOrgThreadPool",
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "12000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "75"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "7000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "15000"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "5")
            }
    )
    public List<License> getLicensesByOrg(Integer organizationId) {
        log.info("LicenseService.getLicensesByOrg Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        GeneralUtils.randomlyRunLong(11000);

        License license = new License();
        license.setOrganizationId(organizationId);
        return this.mapper.select(license);
    }

    @Override
    public License getLicense(Integer organizationId, Integer licenseId, String clientType) {
        License license = this.mapper.selectByPrimaryKey(licenseId);

        Organization org = this.retrieveOrgInfo(organizationId, clientType);
        if (org != null) {
            license.setOrganizationName(org.getName());
            license.setContactName(org.getContactName());
            license.setContactEmail(org.getContactEmail());
            license.setContactPhone(org.getContactPhone());
        }
        license.setRemark(config.getExampleProperty());

        return license;
    }

    @Override
    public int saveLicense(License license) {
        return this.mapper.insert(license);
    }

    private Organization retrieveOrgInfo(Integer orgId, String clientType) {
        Organization org;
        switch (clientType) {
            case "feign":
                log.info("I am using the feign client");
                org = organizationFeignClient.getOrg(orgId);
                break;
            case "rest":
                log.info("I am using the rest client");
                org = organizationRestClient.getOrg(orgId);
                break;
            case "discovery":
                log.info("I am using the discovery client");
                org = organizationDiscoveryClient.getOrg(orgId);
                break;
            default:
                org = organizationRestClient.getOrg(orgId);
        }
        return org;
    }

    /**
     * HystrixCommand的回退方法
     * @param organizationId
     * @return
     */
    private List<License> buildFallbackLicenseList(Integer organizationId) {
        List<License> fallbackList = new ArrayList<>();
        License license = new License();
        license.setId(0);
        license.setOrganizationId(organizationId);
        license.setProductName("Sorry no licensing information currently available");

        fallbackList.add(license);
        return fallbackList;
    }
}
