package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.FactoryBean;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry{

    /** 存放由FactoryBeans创建的单例对象，作为缓存
     * 注意，存的不是FactoryBeans对象，而是由它们创建的对象
     */
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCacheObjectForFactoryBean(String beanName) {
        Object object = factoryBeanObjectCache.get(beanName);
        return object == NULL_OBJECT ? null : object;
        // 这里的NULL_OBJECT不代表没beanName这个key,相反，是有这个key,但对应的是空对象，ConcurrentHashMap不能存放null值于是使用这个来标记
        // 如果不存在beanName这个key，object自然会是null
    }

    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) throws BeansException {
        if (factoryBean.isSingleton()) {
            // 开发人员想用factoryBean创建的bean是单例模式的bean
            // 先从缓存取，没有则调用factoryBean的getObject()，得到后放入缓存
            Object object = factoryBeanObjectCache.get(beanName);
            // 未缓存beanName对应的单例bean实例
            if (object == null) {
                object = factoryBean.getObject();
                factoryBeanObjectCache.put(beanName, object == null ? NULL_OBJECT : object);
            }
            return object == NULL_OBJECT ? null : object;
        }
        return doGetObjectFromFactoryBean(factoryBean, beanName);
    }

    private Object doGetObjectFromFactoryBean(final FactoryBean factoryBean, final String beanName) {
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation", e);
        }
    }
}
