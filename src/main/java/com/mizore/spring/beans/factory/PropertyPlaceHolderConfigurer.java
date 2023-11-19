package com.mizore.spring.beans.factory;

import cn.hutool.core.util.StrUtil;
import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.PropertyValue;
import com.mizore.spring.beans.PropertyValues;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.mizore.spring.beans.factory.support.ConfigurableListableBeanFactory;
import com.mizore.spring.core.io.DefaultResourceLoader;
import com.mizore.spring.core.io.Resource;
import java.io.IOException;
import java.util.Properties;

/**
 * 在所有BeanDefinition加载完成后，实例化bean对象之前，修改 BeanDefinition： 处理占位符——用配置文件的属性替换掉占位符表示的字符串值
 */
public class PropertyPlaceHolderConfigurer implements BeanFactoryPostProcessor {


    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    private String location;
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            // 加载属性文件
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();   // 底层是HashTable
            properties.load(resource.getInputStream());

            // 根据beanDefinitionName拿到beanDefinition，处理beanDefinition中使用占位符的字符串属性
            String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (String beanName : beanDefinitionNames) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
                PropertyValues propertyValues = beanDefinition.getPropertyValues();
                for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
                    Object value = propertyValue.getValue();
                    if (!(value instanceof String strVal)) {
                        continue;
                    }
                    StringBuilder buffer = new StringBuilder(strVal);
                    int startIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_PREFIX), stopIdx = strVal.indexOf(DEFAULT_PLACEHOLDER_SUFFIX);
                    if (startIdx != -1 && stopIdx != -1 && startIdx < stopIdx) {
                        // 获取占位符中的属性名
                        String propKey = strVal.substring(startIdx + 2, stopIdx);
                        // 去属性配置文件中找这个属性
                        String propVal = properties.getProperty(propKey);
                        if (StrUtil.isBlank(propVal)) {
                            throw new BeansException("占位符内的属性名有误！！");
                        }
                        buffer.replace(startIdx, stopIdx + 1, propVal);
                        propertyValue.setValue(buffer.toString());
                    }
                }
            }
        } catch (IOException e) {
            throw new BeansException("Could not load properties", e);
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
