package com.taoing.organizationservice.service;

import com.taoing.organizationservice.model.Organization;

public interface OrganizationService {

    Organization getOrg(Integer orgId);

    Organization saveOrg(Organization org);

    void updateOrg(Organization org);

    void deleteOrg(Integer orgId);
}
