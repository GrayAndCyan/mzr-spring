package com.mizore.spring.context.support;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.core.io.DefaultResourceLoader;
import com.mizore.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.mizore.spring.beans.factory.config.BeanPostProcessor;
import com.mizore.spring.beans.factory.support.ConfigurableListableBeanFactory;
import com.mizore.spring.context.ApplicationEvent;
import com.mizore.spring.context.ApplicationListener;
import com.mizore.spring.context.ConfigurableApplicationContext;
import com.mizore.spring.context.event.ApplicationEventMulticaster;
import com.mizore.spring.context.event.ContextCloseEvent;
import com.mizore.spring.context.event.ContextRefreshEvent;
import com.mizore.spring.context.event.SimpleApplicationEventMulticaster;

import java.util.Collection;
import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {

    private static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationEventMulticaster applicationEventMulticaster;
    @Override
    public void registerShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        // 发布容器关闭事件
        publishEvent(new ContextCloseEvent(this));
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

        // 2. 获取BeanFactory     getBeanFactory()是一个模板方法，实现下放给子类AbstractRefreshableApplicationContext
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        // 3. 添加ApplicationContextAwareProcessor，让继承ApplicationContextAware的bean能够感知到所属的这个ApplicationContext
        // 具体做法是执行bean初始化前的postProcessBeforeInitialization，将添加ApplicationContext set进去
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        // 4. 在Bean实例化之前，执行BeanFactoryPostProcessor （调用 作为bean 注册在上下文的factory后置处理器们）
        invokeBeanFactoryPostProcessors(beanFactory);

        // 5. BeanPostProcessor 需要在其他Bean对象实例化之前执行注册操作
        registryBeanProcessors(beanFactory);

        // 6. 初始化事件广播器
        initApplicationEventMulticaster();

        // 7. 注册事件监听者
        registerListeners();

        // 8. 提前实例化单例bean对象
        beanFactory.preInstantiateSingletons();

        // 9. 发布容器刷新完成事件
        finishRefresh();
    }

    private void finishRefresh() {
        publishEvent(new ContextRefreshEvent(this));
    }


    private void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    private void registerListeners() {
        Collection<ApplicationListener> listeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener listener : listeners) {
            applicationEventMulticaster.addApplicationListener(listener);
        }

    }
    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
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
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
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
