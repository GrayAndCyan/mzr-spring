package com.mizore.spring.aop.framework.autoproxy;

import com.mizore.spring.aop.*;
import com.mizore.spring.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.mizore.spring.aop.framework.ProxyFactory;
import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.PropertyValues;
import com.mizore.spring.beans.factory.BeanFactory;
import com.mizore.spring.beans.factory.BeanFactoryAware;
import com.mizore.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.mizore.spring.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import java.util.Collection;

public class DefaultAdvisorAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    // 在bean实例化之前调用，尝试不去走正常实例化bean逻辑而去创建用于aop的代理对象
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        if (isInfrastructureClass(beanClass)) {
            return null;
        }

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            ClassFilter classFilter = advisor.getPointCut().getClassFilter();
            if (!classFilter.matches(beanClass)) {
                continue;
            }
            // 类型匹配 -> 封装advisedSupport，创建aop代理bean对象
            AdvisedSupport advisedSupport = new AdvisedSupport();
            TargetSource targetSource = null;
            try {
                targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setProxyTargetClass(false);
            advisedSupport.setMethodMatcher(advisor.getPointCut().getMethodMather());
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());

            return new ProxyFactory(advisedSupport).getProxy();
        }
        return null;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    private boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass) || Pointcut.class.isAssignableFrom(beanClass) || Advisor.class.isAssignableFrom(beanClass);
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
