package com.taoing.authentication.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ServiceConfig {

    @Value("${jwt.signing.key}")
    private String jwtSigningKey = "";
}
