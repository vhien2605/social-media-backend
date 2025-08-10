package com.hien.back_end_app.utils.validators;


import com.hien.back_end_app.exceptions.AppException;
import com.hien.back_end_app.utils.anotations.PreSocketAuthorize;
import com.hien.back_end_app.utils.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

@Aspect
@Component
@Slf4j
public class PreSocketAuthorizeHandler {
    @Around("@annotation(preSocketAuthorize)")
    public void handler(ProceedingJoinPoint joinPoint, PreSocketAuthorize preSocketAuthorize) throws Throwable {
        // check role from accessor
        // find accessor from argument list
        log.info("-------------------------------------------preSocketAuthorize handler starts-----------------------------------------");

        SimpMessageHeaderAccessor accessor = null;
        for (Object argument : joinPoint.getArgs()) {
            if (argument instanceof SimpMessageHeaderAccessor neededInstance) {
                accessor = neededInstance;
                break;
            }
        }
        if (accessor == null) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Principal principal = accessor.getUser();
        if (!(principal instanceof Authentication)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        Collection<? extends GrantedAuthority> authorities = ((Authentication) principal).getAuthorities();
        Set<String> requiredAuthorities = Set.of(preSocketAuthorize.authorities());
        boolean isPassed = authorities.stream()
                .anyMatch(ga -> requiredAuthorities.contains(ga.getAuthority()));
        if (!isPassed) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        joinPoint.proceed();
    }
}
