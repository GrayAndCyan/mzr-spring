package com.mizore.spring.aop;

import com.mizore.spring.util.ClassUtils;

// 包装目标对象
public class TargetSource {

    private final Object targetObject;


    public TargetSource(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    /**
     * 获取target对象的接口信息
     * 这个target可能是jdk代理创建的也可能是cglib创建，为了保证都能正确地得到结果，需要判断代理类型
     */
    public Class<?>[] getTargetClass() {
        Class<?> clazz = this.targetObject.getClass();
        clazz = ClassUtils.isProxySubClass(clazz) ? clazz.getSuperclass() : clazz;
        return clazz.getInterfaces();
    }
}
