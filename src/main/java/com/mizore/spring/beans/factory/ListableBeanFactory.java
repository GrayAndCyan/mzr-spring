package com.mizore.spring.beans.factory;

import com.mizore.spring.beans.BeansException;

import java.util.Map;

public interface ListableBeanFactory extends BeanFactory{
    /**
     * 按照类型返回bean实例
     * @param type
     * @return
     * @param <T>
     * @throws BeansException
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

    /**
     * 返回注册表中所有的bean名称
     * @return
     */
    String[] getBeanDefinitionNames();

}
