package com.a301.theknight.global.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("(within(com.a301.theknight.domain.game.api..*) || within(com.a301.theknight.domain.player.api.PlayerApi))" +
            "&& !within(com.a301.theknight.domain.game.api.GameApi)")
    public void onWebSocketApi() { }

    @Pointcut("within(com.a301.theknight.domain.game.service..*) && !within(com.a301.theknight.domain.game.service.GameService)")
    public void onWebSocketService() { }

    @Pointcut("within(com.a301.theknight.domain.game.template..*)")
    public void onDataTemplateService() { }

    @Pointcut("within(com.a301.theknight.domain.common.service.SendMessageService)")
    public void onMessageService() { }

    @Pointcut("within(com.a301.theknight.domain.auth.service..*)")
    public void onSecurityService() { }

    @Around("within(com.a301.theknight..api..*) && !onWebSocketApi()")
    public Object loggingRestApi(ProceedingJoinPoint joinPoint) throws Throwable {
        String params = getParams(joinPoint);
        if (params.contains("password")) {
            params = changePasswordLog(params);
        }
        log.info(" [REST API START] {}, params = {}", joinPoint.getSignature().getName(), params);
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
            log.info("<<===== [REST API END] {}", value.length() > 100 ? value.substring(0, 100) : value);
        }
    }

    @Around("onWebSocketApi()")
    public Object loggingWebsocketApi(ProceedingJoinPoint joinPoint) throws Throwable {
        String params = getParams(joinPoint);
        log.info("  [WebSocket API START] {}, {}", joinPoint.getSignature().getName(), params);
        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            log.info("<<===== [Websocket API END] {}", joinPoint.getSignature().getName());
        }
    }

    @Around("within(com.a301.theknight.domain..service..*) && !onWebSocketService() && !onMessageService() && !onSecurityService()")
    public Object loggingRestService(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            log.info("  [REST Service START] {}", joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            log.info("  [REST Service END] {}, {}ms", joinPoint.getSignature().getName(), end - start);
        }
    }

    @Around("onWebSocketService() && !onMessageService() && !onSecurityService()")
    public Object loggingWebsocketService(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            log.info("  [WebSocket Service] {} START", joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            log.info("  [WebSocket Service] {} END {}ms", joinPoint.getSignature().getName(), end - start);
        }
    }

    @Around("onDataTemplateService()")
    public Object loggingDataTemplateService(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            log.info("  [Data Service START] {} ", joinPoint.getSignature().getName());
            return joinPoint.proceed();
        } finally {
            long end = System.currentTimeMillis();
            log.info("  [Data Service END] {}, {}ms", joinPoint.getSignature().getName(), end - start);
        }
    }

//    @Around("onMessageService()")
//    public Object loggingMessageService(ProceedingJoinPoint joinPoint) throws Throwable {
//        Signature signature = joinPoint.getSignature();
//        String params = getParams(joinPoint);
//
//        return joinPoint.proceed();
//    }

    private String changePasswordLog(String params) {
        int startIndex = params.indexOf("password") + 9;
        String tmp = params.substring(startIndex);
        int lastIndex = tmp.indexOf(",");
        if (lastIndex < 0 || lastIndex < startIndex)
            lastIndex = params.length();
        String substring = params.substring(startIndex, lastIndex);
        return params.replace(substring, "xxxxxxxxxxxxx");
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
