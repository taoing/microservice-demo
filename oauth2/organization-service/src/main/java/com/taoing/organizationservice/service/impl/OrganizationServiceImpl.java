package com.taoing.organizationservice.service.impl;

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

    @Autowired
    public void setMapper(OrganizationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Organization getOrg(Integer orgId) {
        return this.mapper.selectByPrimaryKey(orgId);
    }

    @Override
    public void saveOrg(Organization org) {
        this.mapper.insert(org);
    }

    @Override
    public void updateOrg(Organization org) {
        this.mapper.updateByPrimaryKey(org);
    }

    @Override
    public void deleteOrg(Integer orgId) {
        this.mapper.deleteByPrimaryKey(orgId);
    }
}
