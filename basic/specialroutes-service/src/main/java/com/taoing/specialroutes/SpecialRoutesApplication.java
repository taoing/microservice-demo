package com.taoing.specialroutes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@MapperScan(basePackages = {"com.taoing.specialroutes.mapper"})
public class SpecialRoutesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpecialRoutesApplication.class, args);
    }

    /**
     * 构建基于ribbon的RestTemplate bean
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
