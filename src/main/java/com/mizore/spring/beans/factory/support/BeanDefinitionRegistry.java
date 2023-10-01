package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.factory.config.BeanDefinition;

public interface BeanDefinitionRegistry {
    void registryBeanDefinition(String beanName, BeanDefinition beanDefinition);

    boolean containsBeanDefinition(String beanName);
}
