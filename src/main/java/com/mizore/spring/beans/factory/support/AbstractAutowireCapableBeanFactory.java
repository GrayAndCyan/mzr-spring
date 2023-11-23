package com.mizore.spring.beans.factory.support;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.PropertyValue;
import com.mizore.spring.beans.PropertyValues;
import com.mizore.spring.beans.factory.*;
import com.mizore.spring.beans.factory.aware.Aware;
import com.mizore.spring.beans.factory.config.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 实例化bean类
 */
@Slf4j
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new ByteBuddySubClassingInstantiationStrategy();
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        // 在实例化前解析并处理beanDefinition
        Object bean;
        if ((bean = resolveBeanBeforeInstantiation(beanName, beanDefinition)) != null) {
            // 得到代理bean对象，不走doCreateBean的实例化初始化流程
            return bean;
        }

        return doCreateBean(beanName, beanDefinition, args);
    }

    private Object doCreateBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object beanObject;
        try {
            // bean实例化
            beanObject = createBeanInstance(beanName, beanDefinition, args);

            // 处理循环依赖，将实例化后的bean对象提前放入缓存中暴露出来
            if (beanDefinition.isSingleton()) {
                Object finalBean = beanObject;
                addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, beanDefinition, finalBean));
            }

            // 实例化后判断
            boolean continueWithPropertyPopulation = applyBeanPostProcessorsAfterInstantiation(beanName, beanObject);
            if (!continueWithPropertyPopulation) {
                return beanObject;
            }

            // 在设置bean属性之前，允许BeanPostProcessor 修改属性值，比如 AutoWiredAnnotationBeanPostProcess
            applyBeanProcessorsBeforeApplyingPropertyValues(beanDefinition, beanObject, beanName);
            // 填充bean的属性
            applyPropertyValues(beanName, beanObject, beanDefinition);
            // 执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            beanObject = initializeBean(beanName, beanObject, beanDefinition);
            // 此后，beanObject可能是aop代理对象
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed",e);
        }
        // 注册实现了DisposableBean接口的对象
        registerDisposableBeanIfNecessary(beanName,beanObject, beanDefinition);

        // 单例bean判断，是单例则放入一级缓存同时从二三级移除
        if (beanDefinition.isSingleton()) {
            registerSingleton(beanName, beanObject);
        }
        return beanObject;
    }

    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) {
        Object exposedObject = bean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                exposedObject = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).getEarlyBeanReference(exposedObject, beanName);
                if (null == exposedObject) return exposedObject;
            }
        }

        return exposedObject;
    }

    /**
     * Bean 实例化后对于返回 false 的对象，不在执行后续设置 Bean 对象属性的操作
     *
     * @param beanName
     * @param bean
     * @return
     */
    private boolean applyBeanPostProcessorsAfterInstantiation(String beanName, Object bean) {
        boolean continueWithPropertyPopulation = true;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor = (InstantiationAwareBeanPostProcessor) beanPostProcessor;
                if (!instantiationAwareBeanPostProcessor.postProcessAfterInstantiation(bean, beanName)) {
                    continueWithPropertyPopulation = false;
                    break;
                }
            }
        }
        return continueWithPropertyPopulation;
    }

    /**
     * 尝试创建用于aop的代理bean对象，若这个bean未使用aop增强则返回null,执行doCreateBean的流程
     * @return 若这个bean未使用aop增强则返回null，若这个bean是需要使用aop的则创建并返回织入横切逻辑的代理类的对象
     */
    private Object resolveBeanBeforeInstantiation(String name, BeanDefinition beanDefinition) {
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), name);
        if (bean != null) {
            // 实例化了，但是是织入横切逻辑的代理对象
            bean = applyBeanPostProcessorsAfterInitialization(bean, name);
        }
        return bean;
    }

    private Object applyBeanPostProcessorsBeforeInstantiation(Class beanClass, String name) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, name);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    protected void applyBeanProcessorsBeforeApplyingPropertyValues(BeanDefinition beanDefinition, Object bean, String beanName) {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor) {
                PropertyValues propertyValues = instantiationAwareBeanPostProcessor.postProcessPropertyValues(beanDefinition.getPropertyValues(), bean, beanName);
                for (PropertyValue pv : propertyValues.getPropertyValues()) {
                    beanDefinition.getPropertyValues().addPropertyValue(pv);
                }
            }
        }
    }

    private void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        // 非singleton bean 不执行销毁方法
        if (!beanDefinition.isSingleton()) {
            return;
        }
        if (bean instanceof DisposableBean || StrUtil.isNotEmpty(beanDefinition.getDestroyMethodName())) {
            registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanName, beanDefinition ));
        }
    }


    protected Object createBeanInstance(String name, BeanDefinition beanDefinition, Object[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor constructorToUse = null;
        // 根据args查询合适的构造器
        if (args != null) {
            constructorToUse = findConstructor(beanDefinition.getBeanClass().getConstructors() , args);
        }
        // 根据指定实例化策略，实例化bean
        log.info("实例化 " + name);
        return instantiationStrategy.instantiate(beanDefinition, name, constructorToUse, args);
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

    /**
     * 经过applyBeanPostProcessorsAfterInitialization，initializeBean返回的可能是aop代理对象
     */
    private Object initializeBean(String beanName, Object beanObject, BeanDefinition beanDefinition) {

        // 1. bean可能实现了感知接口，在此判断并调用感知方法
        invokeAwareMethods(beanName, beanObject);

        // 2. 执行BeanPostProcessor Before处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(beanObject, beanName);

        // 3. 调用初始化方法
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Invocation of init method of bean [" + beanName + "] failed!!", e);
        }

        // 4. 执行BeanPostProcessor After处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return wrappedBean;
    }

    /**
     * bean可能实现了感知接口，在此判断并调用感知方法
     */
    private void invokeAwareMethods(String beanName, Object beanObject) {
        if (beanObject instanceof Aware) {
            if (beanObject instanceof BeanFactoryAware) {
                ((BeanFactoryAware) beanObject).setBeanFactory(this);
            }
            if (beanObject instanceof BeanClassLoaderAware) {
                ((BeanClassLoaderAware) beanObject).setBeanClassLoader(getBeanClassLoader());
            }
            if (beanObject instanceof BeanNameAware) {
                ((BeanNameAware) beanObject).setBeanName(beanName);
            }
        }
    }

    private  void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws Exception {
        // 实现接口的方式。
        if (bean instanceof InitializingBean) {
            // 调用接口规范的afterPropertiesSet()方法
            ((InitializingBean)bean).afterPropertiesSet();
            return;
        }

        // init-method配置信息
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName)) {
            Method initMethod = beanDefinition.getBeanClass().getMethod(initMethodName);
            if (initMethod == null) {
                throw new BeansException("Could not find an init method named '" +
                        initMethodName + "' on bean with name '" + beanName + "'!!");
            }
            initMethod.invoke(bean);
        }
    }
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            Object current = beanPostProcessor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
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

    public void setInstantiationStrategy(InstantiationStrategy instantiationStrategy) {
        this.instantiationStrategy = instantiationStrategy;
    }

    public InstantiationStrategy getInstantiationStrategy() {
        return instantiationStrategy;
    }
}
