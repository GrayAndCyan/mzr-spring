package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.DisposableBean;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.config.SingletonBeanRegistry;

import java.beans.Beans;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    /**
     * 空单例对象的内部标记：
     * 用作ConcurrentHashMap（不支持 null 值）的标记值。
     */
    protected final static Object NULL_OBJECT = new Object();

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();
    private final Map<String, DisposableBean> disposableBeanObjects = new ConcurrentHashMap<>();
    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public boolean containsSingleton(String name) {
        return singletonObjects.containsKey(name);
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeanObjects.put(beanName, bean);
    }

    public void destroySingletons() {
        Object[] disposableBeanNames = disposableBeanObjects.keySet().toArray();
        // 边遍历边移除 并调用bean销毁方法 倒序遍历
        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            Object beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeanObjects.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name [" + beanName + "] throw an exception!!", e);
            }
        }

    }
}
