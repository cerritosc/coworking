package com.cuscatlan.coworking.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationProperties {

    private String name;

}