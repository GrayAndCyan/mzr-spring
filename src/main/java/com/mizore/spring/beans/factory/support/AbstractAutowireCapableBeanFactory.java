package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.config.BeanDefinition;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例化bean类
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{


    @Override
    protected Object createBean(String name, BeanDefinition beanDefinition) {
        Object beanObject;
        try {
            beanObject = beanDefinition.getBeanClass().getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed",e);
        }
        // 放入单例bean对象缓存
        registerSingleton(name, beanObject);
        return beanObject;
    }


}
