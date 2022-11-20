package com.a301.theknight.global.aop;

import com.a301.theknight.domain.game.util.GameLockUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class ClickAspect {

    private final GameLockUtil gameLockUtil;

    @Around("@annotation(com.a301.theknight.global.aop.annotation.PreventClick)")
    public Object preventMultiClick(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        log.info("[Prevent API] {}", methodName);
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        long gameId = 0L;
        long memberId = 0L;
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals("gameId") && args[i] != null) {
                gameId = (long) args[i];
            } else if (parameterNames[i].equals("memberId") && args[i] != null) {
                memberId = (long) args[i];
            }
        }

        if (gameLockUtil.clickLock(gameId, memberId, methodName)) {
            return joinPoint.proceed();
        }
        log.info("[Prevent Double Click]");
        return null;
    }
}
