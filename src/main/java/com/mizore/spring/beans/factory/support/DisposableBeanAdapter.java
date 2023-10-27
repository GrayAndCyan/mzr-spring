package com.mizore.spring.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.DisposableBean;
import com.mizore.spring.beans.factory.config.BeanDefinition;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;
    private final String beanName;

    private String destroyMethodName;

    // 在这里 被适配对象是 beanDefinition ，调用方想要只是调用 destroy() 就完成bean销毁方法的执行工作。
    // beanDefinition 提供的不是destroy()这个接口，而是 destroyMethodName ，那么由 DisposableBeanAdapter 去做「持有功能提供者，统一对外接口」的工作
    public DisposableBeanAdapter(Object bean, String beanName, BeanDefinition beanDefinition) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyMethodName = beanDefinition.getDestroyMethodName();
    }

    @Override
    public void destroy() throws Exception {

        // 通过继承 DisposableBean 的方式 定义与指明销毁方法
        if (bean instanceof DisposableBean) {
            ((DisposableBean)bean).destroy();
            return;
        }

        // 通过配置方法名指明
        if (StrUtil.isNotEmpty(destroyMethodName)) {
            Method method = bean.getClass().getMethod(destroyMethodName);
            if (method == null) {
                throw new BeansException("Could not find an destroy method named '" +
                        destroyMethodName + "' on bean with name '" + beanName + "'!!");
            }
            method.invoke(bean);
        }



    }
}
