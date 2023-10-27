package com.mizore.spring.context;

import com.mizore.spring.beans.BeansException;


public interface ConfigurableApplicationContext extends ApplicationContext{
    /**
     * 刷新容器
     * @throws BeansException
     */
    void refresh() throws BeansException;

    void registerShutDownHook();

    void close();
}
