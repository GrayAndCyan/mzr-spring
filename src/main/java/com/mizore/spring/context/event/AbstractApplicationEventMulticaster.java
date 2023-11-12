package com.mizore.spring.context.event;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.BeanFactory;
import com.mizore.spring.beans.factory.BeanFactoryAware;
import com.mizore.spring.context.ApplicationEvent;
import com.mizore.spring.context.ApplicationListener;
import com.mizore.spring.util.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    private Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    private BeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>)listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    /**
     * 获取对applicationEvent事件感兴趣的所有监听者
     * @param event 监听的事件
     * @return 对applicationEvent事件感兴趣的所有监听者的集合
     */
    protected Collection<ApplicationListener> getApplicationListeners(ApplicationEvent event) {
        LinkedList<ApplicationListener> allListeners = new LinkedList<>();
        for (ApplicationListener<ApplicationEvent> listener : applicationListeners) {
            if (supportsEvent(listener, event)) {
                allListeners.add(listener);
            }
        }
        return allListeners;
    }

    /**
     * 判断listener是否对event感兴趣
     * 实际是判断listener实现的ApplicationListener接口的泛型类型能否被event类型对象赋值
     */
    private boolean supportsEvent(ApplicationListener<ApplicationEvent> listener, ApplicationEvent event) {
        // 1. 用反射拿到listener实现的ApplicationListener接口的泛型类型
        // 1.1 获取目标class：实例化策略不同， 目标class不同
        Class<? extends ApplicationListener> listenerClass = listener.getClass();
        Class<?> targetClass = ClassUtils.isProxySubClass(listenerClass) ? listenerClass.getSuperclass() : listenerClass;
        // 1.2 拿到ApplicationListener接口对象
        Type genericInterface = targetClass.getGenericInterfaces()[0];
        // 1.3 拿到参数化类型
        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];
        String typeName = actualTypeArgument.getTypeName();
        Class<?> typeClass;
        try {
            typeClass = Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name: " + typeName);
        }
        return typeClass.isAssignableFrom(event.getClass());
    }
}
