package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.factory.config.BeanDefinition;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.NoOp;

import java.lang.reflect.Constructor;

public class CglibSubClassingInstantiationStrategy implements InstantiationStrategy{
    @Override
    public Object instantiate(BeanDefinition beanDefinition, String beanName, Constructor constructor, Object[] args) {
        // Enhancer 是 CGLIB 库中的类，用于动态生成类的子类。
        Enhancer enhancer = new Enhancer();
        //设置被增强类的父类为 beanDefinition.getBeanClass()，以创建它的增强子类
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setCallback(new NoOp() {
            @Override
            public int hashCode() {
                return super.hashCode();
            }
        });
        if (constructor == null) {
            // 无参构造
            return enhancer.create();
        }
        // 指定含参构造器构造实例
        return enhancer.create(constructor.getParameterTypes(), args);
    }
}
