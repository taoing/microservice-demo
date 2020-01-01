package com.taoing.organizationservice.controller;

import com.taoing.organizationservice.model.Organization;
import com.taoing.organizationservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateOrg(@RequestBody Organization org) {
        this.service.updateOrg(org);
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public void saveOrg(@RequestBody Organization org) {
        this.service.saveOrg(org);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestBody Organization org) {
        this.service.deleteOrg(org);
    }
}
