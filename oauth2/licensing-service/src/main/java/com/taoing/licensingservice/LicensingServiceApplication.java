package com.taoing.licensingservice;

import com.taoing.licensingservice.utils.OAuth2TokenInterceptor;
import com.taoing.licensingservice.utils.UserContextInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateFactory;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EnableResourceServer
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
@MapperScan(basePackages = {"com.taoing.licensingservice.mapper"})
public class LicensingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }

    /**
     * 工厂提供OAuth2RestTemplate bean, 支持OAuth2调用,
     * 处理OAuth2访问令牌的传递, 将传入调用的Authorization头添加到出站调用请求头中
     * OAuth2RestTemplate不支持使用@LoadBalanced 集成ribbon
     * @return
     */
    @Bean
    public OAuth2RestTemplate OAuth2RestTemplate(UserInfoRestTemplateFactory factory) {
        return factory.getUserInfoRestTemplate();
    }


    /**
     * 构建基于ribbon的RestTemplate bean,
     * 加入自定义拦截器, 向出站调用请求中添加token
     *
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
        // 向restTemplate添加自定义的请求拦截器
        if (interceptors.isEmpty()) {
            List<ClientHttpRequestInterceptor> interceptors1 = new ArrayList<>();
            interceptors1.add(new UserContextInterceptor());
            interceptors1.add(new OAuth2TokenInterceptor());
            template.setInterceptors(interceptors1);
//            template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
        } else {
            interceptors.add(new UserContextInterceptor());
            interceptors.add(new OAuth2TokenInterceptor());

            template.setInterceptors(interceptors);
        }
        return template;
    }
}
