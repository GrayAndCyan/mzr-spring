package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory{

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
    public BeanDefinition getBeanDefinition(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        if (beanDefinition == null) throw new BeansException("No bean nameed " + name + "is definied");
        return beanDefinition;
    }

    /**
     * 提前实例化单例 ，也就是提前先getBean一遍map中的beanDefinition,bean会在getBean里实例化
     */
    @Override
    public void preInstantiateSingletons() {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }


    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> res = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (type.isAssignableFrom(beanDefinition.getBeanClass())) {
                res.put(beanName, (T)getBean(beanName));
            }
        });
        return res;
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(String[]::new);
    }
}
