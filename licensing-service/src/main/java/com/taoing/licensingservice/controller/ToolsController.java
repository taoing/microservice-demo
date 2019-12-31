package com.taoing.licensingservice.controller;

import com.taoing.licensingservice.service.DiscoveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/v1/tools")
public class ToolsController {

    private DiscoveryService discoveryService;

    @Autowired
    public void setDiscoveryService(DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    @RequestMapping(value = "/eureka/services", method = RequestMethod.GET)
    public List<String> getEurekaServices() {
        return this.discoveryService.getEurekaServices();
    }

    @RequestMapping(value = "/test404", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "404未找到")
    public void test404() {
        log.info("status code 404 test...");
    }
}
