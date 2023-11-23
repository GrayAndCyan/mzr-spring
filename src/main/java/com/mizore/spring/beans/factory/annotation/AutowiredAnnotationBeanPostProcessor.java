package com.mizore.spring.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.PropertyValues;
import com.mizore.spring.beans.factory.BeanFactory;
import com.mizore.spring.beans.factory.BeanFactoryAware;
import com.mizore.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.mizore.spring.beans.factory.support.ConfigurableListableBeanFactory;
import com.mizore.spring.stereotype.Component;
import com.mizore.spring.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Bean后处理本是应用在bean实例化之后，调用初始化方法前后。
 * 但InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation()方法比较特殊，会在bean实例化之前发挥处理作用
 * 本BeanPostProcess主要是在bean实例化之后调用postProcessPropertyValues()方法发挥根据注解給bean填充属性的作用
 */
@Component
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private ConfigurableListableBeanFactory beanFactory;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }



    // 为bean实例进行注解设置的属性填充
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        clazz = ClassUtils.isProxySubClass(clazz) ? clazz.getSuperclass() : clazz;
        Field[] declaredFields = clazz.getDeclaredFields();

        // 1. 处理 @Value 注解
        for (Field field : declaredFields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation == null) {
                continue;
            }
            String value = valueAnnotation.value();
            value = beanFactory.resolveEmbeddedValue(value);
            BeanUtil.setFieldValue(bean, field.getName(), value);
        }

        // 2. 处理注解 @Autowired
        for (Field field : declaredFields) {
            Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
            if (autowiredAnnotation == null) {
                continue;
            }

            String dependentBeanName = null;
            Object dependentBean = null;
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();
            Qualifier qualifierAnnotation = field.getAnnotation(Qualifier.class);
            if (qualifierAnnotation != null) {
                // 使用了 @Qualifier 注解配合 @Autowired，则优先按照这个注解指明的beanName进行装配
                dependentBeanName = qualifierAnnotation.value();
                dependentBean = beanFactory.getBean(dependentBeanName, fieldType);
                BeanUtil.setFieldValue(bean, fieldName, dependentBean);
            }
            // 未使用@Qualifier 注解配合 @Autowired, 则@Autowired 先根据类型匹配，其次根据属性名匹配
            Map<String, ?> beansOfType = beanFactory.getBeansOfType(fieldType);
            if (beansOfType.size() == 0) {
                throw new BeansException("Injecting bean named [" + beanName + "] error! Cannot find bean of type [" + fieldType + "]");
            } else if (beansOfType.size() > 1) {
                // 该类型的bean有多个，按照属性名再做匹配
                if (!beansOfType.containsKey(fieldName)) {
                    throw new BeansException("Injecting bean named [" + beanName + "] error! Cannot find bean named [" + fieldName + "]");
                }
                dependentBean = beansOfType.get(fieldType);
                BeanUtil.setFieldValue(bean, fieldName, dependentBean);
            } else {
                // 该类型的bean只有一个
                dependentBean = beansOfType.values().toArray()[0];
                BeanUtil.setFieldValue(bean, fieldName, dependentBean);
            }
        }
        return pvs;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> clazz, String beanName) {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }
}
