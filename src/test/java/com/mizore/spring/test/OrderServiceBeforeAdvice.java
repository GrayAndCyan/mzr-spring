package com.mizore.spring.test;

import com.mizore.spring.aop.BeforeAdvice;
import com.mizore.spring.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class OrderServiceBeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("do OrderServiceBeforeAdvice#before");
        System.out.println("before method: " + method);
        method.invoke(target, args);
    }
}
