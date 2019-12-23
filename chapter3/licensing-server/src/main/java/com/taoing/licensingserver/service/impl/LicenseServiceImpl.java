package com.taoing.licensingserver.service.impl;

import com.taoing.licensingserver.mapper.LicenseMapper;
import com.taoing.licensingserver.model.License;
import com.taoing.licensingserver.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LicenseServiceImpl implements LicenseService {

    @Autowired
    private LicenseMapper mapper;

    @Override
    public List<License> getLicensesByOrg(Integer organizationId) {
        License license = new License();
        license.setOrganizationId(organizationId);
        return this.mapper.select(license);
    }

    @Override
    public License getLicense(Integer organizationId, Integer licenseId) {
        License license = this.mapper.selectByPrimaryKey(licenseId);
        return license;
    }

    @Override
    public int saveLicense(License license) {
        return this.mapper.insert(license);
    }
}
