package com.mizore.spring.aop.framework;

import com.mizore.spring.aop.AdvisedSupport;
import org.aopalliance.intercept.MethodInterceptor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advised;

    public JdkDynamicAopProxy(AdvisedSupport advised) {
        this.advised = advised;
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(),
                advised.getTargetSource().getTargetClass(),
                this
        );
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (advised.getMethodMatcher().matches(method, advised.getTargetSource().getClass())) {
            // 方法拦截器中用户定义类横切逻辑，获取通知的拦截器并调用其invoke即可
            MethodInterceptor methodInterceptor = advised.getMethodInterceptor();
            return methodInterceptor.invoke(new ReflectiveMethodInvocation(
                    advised.getTargetSource().getTargetObject(),
                    method,
                    args
                    ));
        }
        return method.invoke(advised.getTargetSource().getTargetObject(), args);
    }
}
