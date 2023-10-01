package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.factory.config.BeanDefinition;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ByteBuddySubClassingInstantiationStrategy implements InstantiationStrategy{
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args)
            throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {

        Class<?> dynamicType = new ByteBuddy()
                .subclass(beanDefinition.getBeanClass())    // 指明要增强的父类
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();

        if (constructor != null) {
            // 使用构造器constructor构造实例
            return dynamicType.getDeclaredConstructor(constructor.getParameterTypes()).newInstance(args);
        } else {
            // 使用默认构造器创建实例
            return dynamicType.getDeclaredConstructor().newInstance();
        }

    }
}








