package com.taoing.organizationservice.service;

import com.taoing.organizationservice.model.Organization;

public interface OrganizationService {

    Organization getOrg(Integer orgId);

    void saveOrg(Organization org);

    void updateOrg(Organization org);

    void deleteOrg(Organization org);
}
