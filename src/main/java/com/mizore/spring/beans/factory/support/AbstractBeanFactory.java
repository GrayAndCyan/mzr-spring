package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.factory.BeanFactory;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory{

    /**
     * 模板方法，完成获取bean的逻辑
     * @param name
     * @return
     */
    @Override
    public Object getBean(String name) {
        // 在DefaultSingletonBeanRegistry的缓存中获取单例bean对象
        if (containsSingleton(name)) {
            log.info("获取到缓存的单例bean对象。");
            return getSingleton(name);
        }

        // 未获取到单例bean对象，拿到bean定义去实例化bean
        BeanDefinition beanDefinition = getBeanDefinition(name);

        return createBean(name, beanDefinition);
    }

    protected abstract Object createBean(String name, BeanDefinition beanDefinition);

    protected abstract BeanDefinition getBeanDefinition(String name);


}
