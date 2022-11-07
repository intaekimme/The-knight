package com.a301.theknight.global.properties.config;

import com.a301.theknight.domain.auth.util.TokenProperties;
import com.a301.theknight.global.webmvc.properties.DomainProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = {TokenProperties.class, DomainProperties.class})
public class PropertiesConfig {
}