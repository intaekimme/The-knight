package com.a301.theknight.global.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    @Around("within(com.a301.theknight..controller..*)")
    public Object loggingController(ProceedingJoinPoint joinPoint) throws Throwable {
        String params = getParams(joinPoint);
        if (params.contains("password")) {
            params = changePasswordLog(params);
        }
        log.info(" [Controller] Method = {}, params = {}", joinPoint.getSignature().getName(), params);
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            ResponseEntity response = (ResponseEntity) result;
            String value = "";
            if (response != null) {
                value = objectMapper.writeValueAsString(response.getBody());
            }
            log.info("<<<<<<< [Response] Response value = {}", value.length() > 100 ? value.substring(0, 100) : value);
        }
    }

    private String changePasswordLog(String params) {
        int startIndex = params.indexOf("password") + 9;
        String tmp = params.substring(startIndex);
        int lastIndex = tmp.indexOf(",");
        if (lastIndex < 0 || lastIndex < startIndex)
            lastIndex = params.length();
        String substring = params.substring(startIndex, lastIndex);
        return params.replace(substring, "xxxxxxxxxxxxx");
    }

    @Around("within(com.a301.theknight.domain..service..*)")
    public Object loggingService(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            log.info(" Service Method = {}, Running Time = {}ms", joinPoint.getSignature().getName(), end - start);
        }
    }

    private String getParams(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        if (parameterNames.length == 0) {
            return "None";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < parameterNames.length; i++) {
            sb.append(parameterNames[i]).append(" : ").append(args[i]);
            if (i != parameterNames.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");

        return sb.toString();
    }
}
