package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.factory.config.BeanDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public interface InstantiationStrategy {

    Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException;
}
