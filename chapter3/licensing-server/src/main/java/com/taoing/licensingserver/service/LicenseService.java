package com.taoing.licensingserver.service;

import com.taoing.licensingserver.model.License;

import java.util.List;

public interface LicenseService {

    List<License> getLicensesByOrg(Integer organizationId);

    License getLicense(Integer organizationId,  Integer licenseId);

    int saveLicense(License license);
}
