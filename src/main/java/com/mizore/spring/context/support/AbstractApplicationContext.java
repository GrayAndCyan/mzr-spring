package com.mizore.spring.context.support;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.core.io.DefaultResourceLoader;
import com.mizore.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.mizore.spring.beans.factory.config.BeanPostProcessor;
import com.mizore.spring.beans.factory.support.ConfigurableListableBeanFactory;
import com.mizore.spring.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    @Override
    public void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        getBeanFactory().destroySingletons();
    }

    /**
     * 这里定义实现过程
     * @throws BeansException
     */
    @Override
    public void refresh() throws BeansException {
        // 1. 创建BeanFactory,并加载BeanDefinition
        refreshBeanFactory();

        // 2. 获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3. 在Bean实例化之前，执行BeanFactoryPostProcessor （调用 作为bean 注册在上下文的factory后置处理器们）
        invokeBeanFactoryPostProcessors(beanFactory);

        // 4. BeanPostProcessor 需要在其他Bean对象实例化之前执行注册操作
        registryBeanProcessors(beanFactory);

        // 5. 提前实例化单例bean对象
        beanFactory.preInstantiateSingletons();
    }

    protected abstract void refreshBeanFactory() throws BeansException;

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        // 获取BeanFactoryPostProcessor类型的所有bean
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        // 依次执行上面获取到的bean的bean工厂后置处理方法
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registryBeanProcessors(ConfigurableListableBeanFactory beanFactory) {
        // 获取BeanPostProcessor类型的所有bean
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }


    @Override
    public Object getBean(String name) {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }
}
