package com.mizore.spring.aop;

// 包装目标对象
public class TargetSource {

    private Object targetObject;

    private Class<?>[] targetClass;

    public TargetSource(Object targetObject) {
        this.targetObject = targetObject;
        this.targetClass = targetObject.getClass().getInterfaces();
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public Class<?>[] getTargetClass() {
        return targetClass;
    }
}
