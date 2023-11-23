package com.mizore.spring.beans.factory.config;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.PropertyValues;


/**
 * Bean后处理本是应用在bean实例化之后，调用初始化方法前后。
 * 但InstantiationAwareBeanPostProcessor的postProcessBeforeInstantiation()方法比较特殊，会在bean实例化之前发挥处理作用
 * 以及它的postProcessPropertyValues()方法，是在bean实例化后，属性填充前执行。
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在实例化目标 Bean 之前应用此 BeanPostProcessor。
     * 返回的 bean 对象可能是代理对象，而不是目标 bean类型实例，
     * 有效地抑制了目标 Bean 的默认实例化。
     * 在bean实例化之前执行
     * @param clazz bean类型
     * @param beanName bean名
     * @return 实例化的bean,但可能是代理对象
     */
    Object postProcessBeforeInstantiation(Class<?> clazz, String beanName);

    /**
     * Perform operations after the bean has been instantiated, via a constructor or factory method,
     * but before Spring property population (from explicit properties or autowiring) occurs.
     * <p>This is the ideal callback for performing field injection on the given bean instance.
     * See Spring's own {@link com.mizore.spring.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor}
     * for a typical example.
     * <p>
     * 在 Bean 对象执行初始化方法之后，执行此方法
     *
     * @param bean
     * @param beanName
     * @return 返回false会终止bean创建后续的操作
     * @throws BeansException
     */
    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

    /**
     * Post-process the given property values before the factory applies them
     * to the given bean. Allows for checking whether all dependencies have been
     * satisfied, for example based on a "Required" annotation on bean property setters.
     * 在 Bean 对象实例化完成后，设置属性操作之前执行此方法
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

    /**
     * 在 Spring 中由 SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference 提供
     * @param bean
     * @param beanName
     * @return
     */
    default Object getEarlyBeanReference(Object bean, String beanName) {
        return bean;
    }
}
