package com.taoing.organizationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @EnableResourceServer 注解告诉 Spring Cloud 和 Spring Security，该服务是受保护
 * 的资源。@EnableResourceServer 强制执行一个过滤器，拦截向服务发出的所有调用，检
 * 查在传入调用 HTTP 头中是否存在 OAuth2 访问令牌(Authorization 头)， 然后回调到在
 * security.oauth2.resource.userInfoUri 中定义的回调 URL，看看令牌是否有效
 */
@EnableResourceServer
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.taoing.organizationservice.mapper"})
public class OrganizationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrganizationServiceApplication.class, args);
    }

}
