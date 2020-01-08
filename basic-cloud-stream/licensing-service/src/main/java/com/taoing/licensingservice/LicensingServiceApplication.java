package com.taoing.licensingservice;

import com.taoing.licensingservice.events.models.OrgChangeModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @EnableBinding 注解服务绑定到Sink接口中定义的输入通道, 消费消息
 */
@Slf4j
@EnableBinding(Sink.class)
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
@MapperScan(basePackages = {"com.taoing.licensingservice.mapper"})
public class LicensingServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(LicensingServiceApplication.class, args);
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

    /**
     * 注解方法监听输入通道上传入的消息
     * @param orgChange
     */
    @StreamListener(Sink.INPUT)
    public void loggerSink(OrgChangeModel orgChange) {
        log.info("Received an {} event for orgId: {}", orgChange.getAction(), orgChange.getOrgId());
    }
}
