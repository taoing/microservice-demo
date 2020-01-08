package com.taoing.licensingservice.service;

import com.taoing.licensingservice.model.License;

import java.util.List;

public interface LicenseService {

    List<License> getLicensesByOrg(Integer organizationId);

    License getLicense(Integer organizationId, Integer licenseId, String clientType);

    int saveLicense(License license);
}
