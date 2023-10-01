package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.core.io.DefaultResourceLoader;
import com.mizore.spring.beans.core.io.ResourceLoader;

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    private ResourceLoader resourceLoader;

    private final BeanDefinitionRegistry registry;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this(new DefaultResourceLoader(), registry);
    }

    public AbstractBeanDefinitionReader(ResourceLoader resourceLoader, BeanDefinitionRegistry registry) {
        this.resourceLoader = resourceLoader;
        this.registry = registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return this.resourceLoader;
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }
}
