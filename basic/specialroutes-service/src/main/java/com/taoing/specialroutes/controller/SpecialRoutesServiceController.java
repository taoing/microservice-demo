package com.taoing.specialroutes.controller;

import com.taoing.specialroutes.model.AbTestingRoute;
import com.taoing.specialroutes.service.AbTestingRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/v1/route")
public class SpecialRoutesServiceController {

    private AbTestingRouteService routeService;

    @Autowired
    public void setRouteService(AbTestingRouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping(value = "/abtesting/{serviceName}")
    public AbTestingRoute abtesting(@PathVariable("serviceName") String serviceName) {
        log.info("Looking up data for serviceName: {}", serviceName);
        return this.routeService.getRoute(serviceName);
    }
}
