package com.taoing.organizationservice.controller;

import com.taoing.organizationservice.model.Organization;
import com.taoing.organizationservice.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping(value = "/v1/organizations")
@RestController
public class OrganizationServiceController {

    private OrganizationService service;

    @Autowired
    public void setService(OrganizationService service) {
        this.service = service;
    }

    @RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
    public Organization getOrg(@PathVariable("orgId") Integer orgId) {
        return this.service.getOrg(orgId);
    }

    @RequestMapping(value = "/{orgId}", method = RequestMethod.PUT)
    public void updateOrg(@PathVariable("orgId") Integer orgId, @RequestBody Organization org) {
        this.service.updateOrg(org);
    }

    @RequestMapping(value = "/{orgId}", method = RequestMethod.POST)
    public void saveOrg(@PathVariable("orgId") Integer orgId, @RequestBody Organization org) {
        this.service.saveOrg(org);
    }

    @RequestMapping(value = "/{orgId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("orgId") Integer orgId) {
        log.info("*** simulating delete orgId: {}***", orgId);

        this.service.deleteOrg(null);
    }
}
