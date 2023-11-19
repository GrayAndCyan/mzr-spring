package com.mizore.spring.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.support.BeanDefinitionRegistry;
import com.mizore.spring.stereotype.Component;
import com.mizore.spring.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Set;

public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider{

    private BeanDefinitionRegistry registry;

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public void doScan(String... basePackages) {
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            // 解析bean的作用域 singleton 、 prototype
            for (BeanDefinition beanDefinition : candidates) {
                String beanScope = resolveBeanScope(beanDefinition);
                if (StrUtil.isNotEmpty(beanScope)) {
                    beanDefinition.setScope(beanScope);
                }
                registry.registryBeanDefinition(determineBeanName(beanDefinition), beanDefinition);
            }
        }
    }

    private String determineBeanName(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Component component = beanClass.getAnnotation(Component.class);
        String beanName = component.value();
        if (beanName.isBlank()) {
            return ClassUtils.getDefaultBeanName(beanClass);
        }
        return beanName;
    }

    private String resolveBeanScope(BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getBeanClass();
        Scope scope = beanClass.getAnnotation(Scope.class);
        if (scope == null) {
            // 未设置@Scope 会使用beanDefinition属性默认值singleton
            return StrUtil.EMPTY;
        }
        return scope.value();
    }
}
