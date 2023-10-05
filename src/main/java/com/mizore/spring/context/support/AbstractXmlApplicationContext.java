package com.mizore.spring.context.support;

import com.mizore.spring.beans.factory.support.DefaultListableBeanFactory;
import com.mizore.spring.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 上下文中对配置信息的加载
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{
    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        // 获取xml配置定位
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            // 使用XmlBeanDefinitionReader读取xml配置
            XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(this, beanFactory);
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    /**
     * 从入口上下文类，拿到配置信息的地址描述
     * @return
     */
    protected abstract String[] getConfigLocations();
}
