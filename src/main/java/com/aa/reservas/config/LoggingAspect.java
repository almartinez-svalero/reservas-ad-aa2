package com.aa.reservas.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.aa.reservas.controller..*(..)) || execution(* com.aa.reservas.service..*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Ejecutando: {}", joinPoint.getSignature().toShortString());
    }

    @AfterReturning("execution(* com.aa.reservas.controller..*(..)) || execution(* com.aa.reservas.service..*(..))")
    public void logAfterReturning(JoinPoint joinPoint) {
        logger.info("Operación finalizada correctamente: {}", joinPoint.getSignature().toShortString());
    }

    @AfterThrowing(pointcut = "execution(* com.aa.reservas.controller..*(..)) || execution(* com.aa.reservas.service..*(..))", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        logger.error("Error en {}: {}", joinPoint.getSignature().toShortString(), ex.getMessage());
    }
}
