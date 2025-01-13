package com.cdw.helper.app.common.configs;

import com.cdw.helper.app.common.utilities.LogUtility;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect Oriented Programming configurations
 */
@Component
@Aspect
public class AOPConfig {

    /**
     * General pointcut for all methods in the helper application
     * except not to log JWT filter functions for security purposes
     */
    @Pointcut("execution(* com.cdw.helper.app..*(..)) && !within(com.cdw.helper.app.common.security.JwtFilter)")
    public void generalPointCut() {
    }


    /**
     * This method is used to print logs before it hits the end point.
     * @param joinpoint contains information about the point
     */
    @Before(value = "generalPointCut()")
    public void before(JoinPoint joinpoint) {
        LogUtility logger = new LogUtility();
        logger.entryLog(joinpoint.getSignature().toString());
    }

    /**
     * This method is used to print logs before it hits the end point.
     * @param joinpoint   This will contain information about the point
     * @param returnValue it contains the return value of the method
     */
    @AfterReturning(value = "generalPointCut()", returning = "returnValue")
    public void afterreturning(JoinPoint joinpoint, Object returnValue) {
        LogUtility logger = new LogUtility();
        String returnVal = (returnValue == null) ? "" : returnValue.toString();
        logger.exitLog(joinpoint.getSignature().toString(), returnVal);
    }

    /**
     * This method is used to print logs before it hits the end point.
     * @param joinpoint This will contain information about the point
     * @param e         It contains exception which is thrown by the method
     */
    @AfterThrowing(value = "generalPointCut()", throwing = "e")
    public void afterthrowing(JoinPoint joinpoint, Exception e) {
        LogUtility logger = new LogUtility();
        logger.info("after method invoked - throwing exception::" + e.getMessage());
    }
}
