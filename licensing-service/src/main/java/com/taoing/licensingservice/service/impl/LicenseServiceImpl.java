package com.taoing.licensingservice.service.impl;

import com.taoing.licensingservice.clients.OrganizationDiscoveryClient;
import com.taoing.licensingservice.clients.OrganizationFeignClient;
import com.taoing.licensingservice.clients.OrganizationRestTemplateClient;
import com.taoing.licensingservice.config.ServiceConfig;
import com.taoing.licensingservice.mapper.LicenseMapper;
import com.taoing.licensingservice.model.License;
import com.taoing.licensingservice.model.Organization;
import com.taoing.licensingservice.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LicenseServiceImpl implements LicenseService {

    private LicenseMapper mapper;

    private OrganizationDiscoveryClient organizationDiscoveryClient;

    private OrganizationRestTemplateClient organizationRestClient;

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
    public void setOrganizationRestClient(OrganizationRestTemplateClient organizationRestClient) {
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

    @Override
    public List<License> getLicensesByOrg(Integer organizationId) {
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
                org = organizationFeignClient.getOrg(orgId);
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
}
