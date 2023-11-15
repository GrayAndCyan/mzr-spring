package com.mizore.spring.test.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class OrderServiceInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            return methodInvocation.proceed();
        } finally {
            System.out.println("监控 - Begin by AOP");
            System.out.println("方法名称： " + methodInvocation.getMethod());
            System.out.println("方法耗时： " + (System.currentTimeMillis() - start) + " ms");
            System.out.println("监控 - end\n");

        }
    }
}
