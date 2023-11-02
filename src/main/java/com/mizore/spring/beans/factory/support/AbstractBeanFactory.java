package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.config.BeanPostProcessor;
import com.mizore.spring.beans.factory.config.ConfigurableBeanFactory;
import com.mizore.spring.util.ClassUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

    public ClassLoader getBeanClassLoader() {
        return classLoader;
    }

    @Override
    public Object getBean(String name) {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) {
        return doGetBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return (T)getBean(name);
    }


    /**
     * 模板方法，完成获取bean的逻辑
     * @param name
     * @param args
     * @return
     */
    protected Object doGetBean(String name, Object[] args) {
        // 在DefaultSingletonBeanRegistry的缓存中获取单例bean对象
        if (containsSingleton(name)) {
            log.info("获取到缓存的单例bean对象: {}",name);
            return getSingleton(name);
        }

        // 未获取到单例bean对象，拿到bean定义去实例化bean
        BeanDefinition beanDefinition = getBeanDefinition(name);

        return createBean(name, beanDefinition, args);
    }


    protected abstract Object createBean(String name, BeanDefinition beanDefinition, Object[] args);

    protected abstract BeanDefinition getBeanDefinition(String name);

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    /**
     * 返回BeanPost处理器列表，这些处理器将应用于 「被这个工厂创建的bean」
     * @return
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }
}
