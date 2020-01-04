package com.taoing.licensingservice.service.impl;

import com.taoing.licensingservice.service.DiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DiscoveryServiceImpl implements DiscoveryService {

    private DiscoveryClient discoveryClient;

    @Autowired
    public void setDiscoveryClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @Override
    public List<String> getEurekaServices() {
        List<String> services = new ArrayList<>();
        discoveryClient.getServices().forEach(serviceName -> {
            discoveryClient.getInstances(serviceName).forEach(instance -> {
                services.add(String.format("%s:%s", serviceName, instance.getUri()));
            });
        });
        return services;
    }
}
