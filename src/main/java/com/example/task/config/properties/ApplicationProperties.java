package com.example.task.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "application")
public record ApplicationProperties(@NestedConfigurationProperty RsaKeyProperties rca) {}
