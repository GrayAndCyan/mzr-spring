package com.mizore.spring.aop;

// Pointcut, 是对JoinPoint的表述  定义两个方法捕捉系统中的JoinPoint
public interface Pointcut {

    // 用于类型匹配
    ClassFilter getClassFilter();

    // 用于方法匹配
    MethodMatcher getMethodMather();

}
