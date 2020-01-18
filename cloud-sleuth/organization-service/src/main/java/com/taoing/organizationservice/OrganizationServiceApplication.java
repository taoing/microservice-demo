package com.taoing.organizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @EnableBinding 注解服务绑定到Source接口中定义的输出通道, 发送消息
 */
@EnableBinding(Source.class)
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.taoing.organizationservice.mapper"})
public class OrganizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizationServiceApplication.class, args);
    }

}
