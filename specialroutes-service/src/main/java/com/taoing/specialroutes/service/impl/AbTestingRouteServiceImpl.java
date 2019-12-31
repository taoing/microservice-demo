package com.taoing.specialroutes.service.impl;

import com.taoing.specialroutes.exception.NoRouteFound;
import com.taoing.specialroutes.mapper.AbTestingRouteMapper;
import com.taoing.specialroutes.model.AbTestingRoute;
import com.taoing.specialroutes.service.AbTestingRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbTestingRouteServiceImpl implements AbTestingRouteService {

    private AbTestingRouteMapper routeMapper;

    @Autowired
    public void setRouteMapper(AbTestingRouteMapper routeMapper) {
        this.routeMapper = routeMapper;
    }

    @Override
    public AbTestingRoute getRoute(String serviceName) {
        AbTestingRoute condition = new AbTestingRoute();
        condition.setServiceName(serviceName);
        AbTestingRoute route = this.routeMapper.selectOne(condition);
        if (route == null) {
            throw new NoRouteFound();
        }
        return route;
    }
}
