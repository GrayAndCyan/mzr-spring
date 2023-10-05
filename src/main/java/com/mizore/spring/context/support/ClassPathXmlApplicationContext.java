package com.mizore.spring.context.support;

import com.mizore.spring.beans.BeansException;

/**
 * 应用上下文实现类
 */
public class ClassPathXmlApplicationContext extends AbstractXmlApplicationContext{

    private String[] configLocations;

    public ClassPathXmlApplicationContext() {
    }

    /**
     * 从xml中加载BeanDefinition，并刷新上下文
     * @param configLocation
     * @throws BeansException
     */
    public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
        this(new String[]{configLocation});
    }

    /**
     * 从xml中加载BeanDefinition,并刷新上下文
     * @param configLocations
     * @throws BeansException
     */
    public ClassPathXmlApplicationContext(String[] configLocations) throws BeansException{
        this.configLocations = configLocations;
        // 刷新上下文
        refresh();
    }

    /**
     * 提供配置文件地址信息
     * @return
     */
    @Override
    protected String[] getConfigLocations() {
        return this.configLocations;
    }
}
