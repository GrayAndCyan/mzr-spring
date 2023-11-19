package com.mizore.spring.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

public class ClassPathScanningCandidateComponentProvider {

    /**
     * 根据包路径扫描出加了@Component注解的组建，注册为beanDefinition
     * @param basePackage 扫描的目标路径
     * @return 扫描到的组件注册的beanDefinition的集合
     */
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        // hutool nb
        Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
        for (Class<?> clazz : classes) {
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            // TODO 为beanDefinition填充属性
            candidates.add(beanDefinition);
        }
        return candidates;
    }
}
