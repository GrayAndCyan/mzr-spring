package com.mizore.spring.beans.factory.config;

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
}
