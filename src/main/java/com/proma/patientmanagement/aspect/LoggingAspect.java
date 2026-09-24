package com.proma.patientmanagement.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Cross-cutting logging with Spring AOP. Every service method is timed and
 * logged without adding logging code to the service itself.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.proma.patientmanagement.service..*(..))")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        long start = System.currentTimeMillis();
        log.info("Entering {}", method);
        try {
            Object result = joinPoint.proceed();
            long elapsed = System.currentTimeMillis() - start;
            log.info("Exiting {} in {} ms", method, elapsed);
            return result;
        } catch (Throwable ex) {
            log.warn("Exception in {}: {}", method, ex.getMessage());
            throw ex;
        }
    }
}
