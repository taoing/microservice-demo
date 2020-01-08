package com.taoing.licensingservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class ServiceConfig {

    @Value("${example.property}")
    private String exampleProperty;

    @Value("${example.my.value}")
    private String myValue;
}
