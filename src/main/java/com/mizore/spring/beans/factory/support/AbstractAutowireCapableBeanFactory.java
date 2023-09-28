package com.mizore.spring.beans.factory.support;



import cn.hutool.core.bean.BeanUtil;
import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.PropertyValue;
import com.mizore.spring.beans.factory.PropertyValues;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.config.BeanReference;

import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * 实例化bean类
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private final InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();
    @Override
    protected Object createBean(String name, BeanDefinition beanDefinition, Object[] args) {
        Object beanObject;
        try {
            // bean实例化
            beanObject = createBeanInstance(name, beanDefinition, args);
            // 填充bean的属性
            applyPropertyValues(name, beanObject, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed",e);
        }
        // 放入单例bean对象缓存
        registerSingleton(name, beanObject);
        return beanObject;
    }

    private void applyPropertyValues(String beanName, Object beanObject, BeanDefinition beanDefinition) {
        try {
            PropertyValues propertyValues = beanDefinition.getPropertyValues();
            for (PropertyValue pv : propertyValues.getPropertyValues()) {
                String name = pv.getName();
                Object value = pv.getValue();
                if (value instanceof BeanReference) {
                    // beanA依赖beanB，获取beanB的实例，来更新value。递归操作。
                    // TODO 未处理循环依赖
                    value = getBean(((BeanReference) value).getBeanName());
                }
                BeanUtil.setFieldValue(beanObject, name, value);
            }
        } catch (Exception e) {
            throw new BeansException("Error setting property values : " + beanName,e);
        }
    }

    protected Object createBeanInstance(String name, BeanDefinition beanDefinition, Object[] args) {
        Object beanObject;
        Constructor constructorToUse = null;
        // 根据args查询合适的构造器
        if (args != null) {
            constructorToUse = findConstructor(beanDefinition.getBeanClass().getConstructors() , args);
        }
        // 根据指定实例化策略，实例化bean
        return instantiationStrategy.instantiate(beanDefinition, name, constructorToUse, args);
    }

    /**
     * 根据args，在constructors中找到参数匹配的构造器
     * @param constructors 类的构造器数组
     * @param args 待入参的参数
     * @return  参数匹配的构造器
     */
    private Constructor findConstructor(Constructor[] constructors, Object[] args) {
        Constructor res = null;
        for (Constructor constructor : constructors) {
            if (constructor.getParameterCount() == args.length) {
                res = constructor;
                break;
            }

        }
        return res;
    }




}
