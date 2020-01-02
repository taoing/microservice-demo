package com.taoing.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.HashMap;
import java.util.Map;

/**
 * @EnableAuthorizationServer 启用OAuth2的认证端点, 作为一个OAuth2的认证服务器,
 * 在spring-security-oauth2的2.4.0.RELEASE版本中, 该注解已过时, 需使用spring security的其他方式
 */
@EnableEurekaClient
@EnableResourceServer
@EnableAuthorizationServer
@RestController
@SpringBootApplication
@MapperScan(basePackages = "com.taoing.authentication.mapper")
public class AuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

    /**
     * 检索与令牌相关的用户信息, token放在Authorization头中
     * @param user
     * @return
     */
    @RequestMapping(value = {"/user"}, produces = "application/json")
    public Map<String, Object> user(OAuth2Authentication user) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("user", user.getUserAuthentication().getPrincipal());
        userInfo.put("authorities", AuthorityUtils.authorityListToSet(user.getUserAuthentication().getAuthorities()));
        return userInfo;
    }

}
