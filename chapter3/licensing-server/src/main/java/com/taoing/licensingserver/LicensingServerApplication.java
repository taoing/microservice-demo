package com.taoing.licensingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@RefreshScope
@MapperScan(basePackages = {"com.taoing.licensingserver.mapper"})
public class LicensingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicensingServerApplication.class, args);
    }

}
