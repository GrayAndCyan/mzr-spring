package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry{

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    // Implementation of BeanDefinitionRegistry interface
    @Override
    public void registryBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    protected BeanDefinition getBeanDefinition(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) throw new BeansException("No bean nameed " + name + "is definied");
        return beanDefinition;
    }


}
