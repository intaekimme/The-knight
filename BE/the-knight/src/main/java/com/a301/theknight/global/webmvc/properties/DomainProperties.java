package com.a301.theknight.global.webmvc.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.List;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(value = "domain", ignoreInvalidFields = true)
public class DomainProperties {
    private final String main;
    private final String local;
}
