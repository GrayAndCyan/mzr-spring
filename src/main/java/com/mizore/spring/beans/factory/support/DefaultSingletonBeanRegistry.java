package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.DisposableBean;
import com.mizore.spring.beans.factory.ObjectFactory;
import com.mizore.spring.beans.factory.config.SingletonBeanRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    /**
     * 空单例对象的内部标记：
     * 用作ConcurrentHashMap（不支持 null 值）的标记值。
     */
    protected final static Object NULL_OBJECT = new Object();

    // 一级缓存 存放成品bean实例
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    // 二级缓存，存放半成品bean实例，也就是bean实例化后但尚未填充属性
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    // 三级缓存，存放ObjectFactory类型工厂对象
    private final Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>(16);

    private final Map<String, DisposableBean> disposableBeanObjects = new ConcurrentHashMap<>();
    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        // 放入一级缓存
        singletonObjects.put(beanName, singletonObject);
        // 从二三级移除
        earlySingletonObjects.remove(beanName); // 由此避免了空指针
        singletonFactories.remove(beanName);    // 由此避免了目标对象重复创建
    }

    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject == null) {
            // 不在一级缓存，到二级缓存（半成品）中去找
            singletonObject = earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                // 二级缓存也没有，说明这个被依赖的bean是代理对象
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();

                    log.info("在三级缓存中获取到名为[{}]的bean对象： {}", beanName, singletonObject);

                    // 把代理对象中的真实对象取出来放入二级缓存
                    earlySingletonObjects.put(beanName, singletonObject);
                    // 从三级缓存中移除代理对象，以后可以直接在二级缓存命中真实对象
                    singletonFactories.remove(beanName);
                } else {
                    log.info("未在三级缓存中获取到名为[{}]的bean对象", beanName);
                }
            } else {
                log.info("在二级缓存中获取到名为[{}]的bean对象： {}", beanName, singletonObject);
            }
        } else {
            log.info("在一级缓存中获取到名为[{}]的bean对象： {}", beanName, singletonObject);

        }
        return singletonObject;
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    @Override
    public boolean containsSingleton(String name) {
        return singletonObjects.containsKey(name) | earlySingletonObjects.containsKey(name) | singletonFactories.containsKey(name);
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
