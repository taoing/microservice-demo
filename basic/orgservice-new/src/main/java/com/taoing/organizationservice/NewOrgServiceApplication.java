package com.taoing.organizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.taoing.organizationservice.mapper"})
public class NewOrgServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewOrgServiceApplication.class, args);
    }

}
