package com.mizore.spring.beans.factory.support;



import cn.hutool.core.bean.BeanUtil;
import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.PropertyValue;
import com.mizore.spring.beans.PropertyValues;
import com.mizore.spring.beans.factory.config.AutowireCapableBeanFactory;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.config.BeanPostProcessor;
import com.mizore.spring.beans.factory.config.BeanReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 实例化bean类
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new ByteBuddySubClassingInstantiationStrategy();
    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) {
        Object beanObject;
        try {
            // bean实例化
            beanObject = createBeanInstance(beanName, beanDefinition, args);
            // 填充bean的属性
            applyPropertyValues(beanName, beanObject, beanDefinition);
            // 执行bean的初始化方法和BeanPostProcessor的前置和后置处理方法
            beanObject = initializeBean(beanName, beanObject, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed",e);
        }
        // 放入单例bean对象缓存
        registerSingleton(beanName, beanObject);
        return beanObject;
    }

    protected Object createBeanInstance(String name, BeanDefinition beanDefinition, Object[] args) throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException {
        Constructor constructorToUse = null;
        // 根据args查询合适的构造器
        if (args != null) {
            constructorToUse = findConstructor(beanDefinition.getBeanClass().getConstructors() , args);
        }
        // 根据指定实例化策略，实例化bean
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

    private Object initializeBean(String beanName, Object beanObject, BeanDefinition beanDefinition) {
        // 1. 执行BeanPostProcessor Before处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(beanObject, beanName);

        // 2. TODO 完成调用初始化方法的方法
        invokeInitMethods(beanName, wrappedBean, beanDefinition);

        // 3. 执行BeanPostProcessor After处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return wrappedBean;
    }

    private  void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {

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
