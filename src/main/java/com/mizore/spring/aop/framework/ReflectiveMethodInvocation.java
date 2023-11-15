package com.mizore.spring.aop.framework;

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public class ReflectiveMethodInvocation implements MethodInvocation{
    // 目标对象
    protected Object targetObject;

    // 方法
    protected Method method;

    // 入参
    protected Object[] arguments;

    public ReflectiveMethodInvocation(Object targetObject, Method method, Object[] arguments) {
        this.targetObject = targetObject;
        this.method = method;
        this.arguments = arguments;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    @Override
    public Object proceed() throws Throwable {
        return method.invoke(targetObject, arguments);
    }

    @Override
    public Object getThis() {
        return targetObject;
    }

    @Override
    public AccessibleObject getStaticPart() {
        return method;
    }
}
