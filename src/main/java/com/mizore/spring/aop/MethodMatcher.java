package com.mizore.spring.aop;

import java.lang.reflect.Method;


public interface MethodMatcher {

    // 匹配方法
    boolean matches(Method method, Class<?> targetClass);
}
