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

    // 在bean实例化之前调用
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        return pvs;
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
        Class<?> beanClass = bean.getClass();
        if (isInfrastructureClass(beanClass)) {
            return bean;
        }

        Collection<AspectJExpressionPointcutAdvisor> advisors = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class).values();
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            // advisor封装了切点和横切逻辑
            ClassFilter classFilter = advisor.getPointCut().getClassFilter();
            if (!classFilter.matches(beanClass)) {
                continue;
            }
            // 类型匹配 -> 封装advisedSupport，创建aop代理bean对象
            AdvisedSupport advisedSupport = new AdvisedSupport();
            /*
            TargetSource targetSource = null;
            try {
                targetSource = new TargetSource(beanClass.getDeclaredConstructor().newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
            // 由反射创建新对象，改为了基于传来的初始化完成之后的bean对象，来创建它的代理对象，如下一行
            TargetSource targetSource = new TargetSource(bean);
            advisedSupport.setTargetSource(targetSource);
            advisedSupport.setProxyTargetClass(false);
            advisedSupport.setMethodMatcher(advisor.getPointCut().getMethodMather());
            advisedSupport.setMethodInterceptor((MethodInterceptor) advisor.getAdvice());

            // 返回代理对象
            return new ProxyFactory(advisedSupport).getProxy();
        }
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
