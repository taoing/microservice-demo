package com.taoing.licensingservice.service;

import java.util.List;

public interface DiscoveryService {

    /**
     * eureka客户端从eureka service获取的服务注册信息
     *
     * @return
     */
    List<String> getEurekaServices();
}
