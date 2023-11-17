package com.mizore.spring.beans.factory.support;

import com.mizore.spring.core.io.Resource;
import com.mizore.spring.core.io.ResourceLoader;

public interface BeanDefinitionReader {

    // 获取加载器加载资源
    ResourceLoader getResourceLoader();

    // 解析资源获取bean信息后，注册bean
    BeanDefinitionRegistry getRegistry();

    void loadBeanDefinitions(Resource resource);
    void loadBeanDefinitions(Resource... resources);
    void loadBeanDefinitions(String location);

    void loadBeanDefinitions(String[] locations);
}
