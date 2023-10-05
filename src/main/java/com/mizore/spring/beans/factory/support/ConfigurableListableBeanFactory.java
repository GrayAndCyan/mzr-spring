package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.factory.ListableBeanFactory;
import com.mizore.spring.beans.factory.config.AutowireCapableBeanFactory;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.config.ConfigurableBeanFactory;

public interface ConfigurableListableBeanFactory extends ConfigurableBeanFactory, ListableBeanFactory, AutowireCapableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName);

    void preInstantiateSingletons();
}
