package com.mizore.spring.beans.factory.config;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.support.ConfigurableListableBeanFactory;

public interface BeanFactoryPostProcessor {
    /**
     * 在所有BeanDefinition加载完成后，实例化Bean对象前，提供修改BeanDefinition的机制
     * @param beanFactory
     * @throws BeansException
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
