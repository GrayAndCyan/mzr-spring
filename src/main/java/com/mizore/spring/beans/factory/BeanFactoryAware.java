package com.mizore.spring.beans.factory;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.aware.Aware;

public interface BeanFactoryAware extends Aware {


    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
