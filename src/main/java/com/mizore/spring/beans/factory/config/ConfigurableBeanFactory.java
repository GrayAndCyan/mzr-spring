package com.mizore.spring.beans.factory.config;

import com.mizore.spring.beans.factory.HierarchicalBeanFactory;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";


    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    void destroySingletons();
}
