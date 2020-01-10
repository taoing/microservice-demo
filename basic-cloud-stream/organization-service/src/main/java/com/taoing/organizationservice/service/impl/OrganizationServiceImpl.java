package com.taoing.organizationservice.service.impl;

import com.taoing.organizationservice.events.source.SimpleSourceBean;
import com.taoing.organizationservice.mapper.OrganizationMapper;
import com.taoing.organizationservice.model.Organization;
import com.taoing.organizationservice.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private OrganizationMapper mapper;

    private SimpleSourceBean simpleSourceBean;

    @Autowired
    public void setMapper(OrganizationMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setSimpleSourceBean(SimpleSourceBean simpleSourceBean) {
        this.simpleSourceBean = simpleSourceBean;
    }

    @Override
    public Organization getOrg(Integer orgId) {
        Organization org = this.mapper.selectByPrimaryKey(orgId);

        this.simpleSourceBean.publishOrgChange("GET", org.getId());
        return org;
    }

    @Override
    public Organization saveOrg(Organization org) {
        this.mapper.insert(org);

        this.simpleSourceBean.publishOrgChange("SAVE", org.getId());
        return org;
    }

    @Override
    public void updateOrg(Organization org) {
        this.mapper.updateByPrimaryKey(org);

        this.simpleSourceBean.publishOrgChange("UPDATE", org.getId());
    }

    @Override
    public void deleteOrg(Integer orgId) {
        this.mapper.deleteByPrimaryKey(orgId);

        this.simpleSourceBean.publishOrgChange("DELETE", orgId);
    }
}
