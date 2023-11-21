package com.mizore.spring.beans.factory.support;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.FactoryBean;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.config.BeanPostProcessor;
import com.mizore.spring.beans.factory.config.ConfigurableBeanFactory;
import com.mizore.spring.util.ClassUtils;
import com.mizore.spring.util.StringValueResolver;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private ClassLoader classLoader = ClassUtils.getDefaultClassLoader();

    /**
     * String resolvers to apply e.g. to annotation attribute values
     */
    private final List<StringValueResolver> embeddedValueResolvers = new ArrayList<>();

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }

    public ClassLoader getBeanClassLoader() {
        return classLoader;
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T)getBean(name);
    }


    /**
     * 模板方法，完成获取bean的逻辑
     * @param name
     * @param args
     * @return
     */
    protected Object doGetBean(String name, Object[] args) throws BeansException {
        // 在DefaultSingletonBeanRegistry的缓存中获取单例bean对象
        if (containsSingleton(name)) {
            log.info("获取到缓存的单例bean对象: {}",name);
//            return getSingleton(name);
            // 不再直接返回获取的单例bean对象，而是检查是不是factoryBean,是的话，返回它所创建的对象；不是则返回原单例bean对象
            return getObjectForBeanInstance(getSingleton(name), name);
        }

        // 未获取到单例bean对象，拿到bean定义去实例化bean
        BeanDefinition beanDefinition = getBeanDefinition(name);

//        return createBean(name, beanDefinition, args);
        // 这里也是，不再直接返回创建的bean对象，而是检查这个对象是不是factoryBean,是的话，返回它所创建的对象；不是则返回原对象
        return getObjectForBeanInstance(createBean(name, beanDefinition, args), name);
    }

    private Object getObjectForBeanInstance(Object beanInstance, String beanName) throws BeansException {
        if (! (beanInstance instanceof FactoryBean<?>)) {
            return beanInstance;
        }
        // beanInstance 是 factoryBean，不返回它，返回它该创建的对象（它该创建什么对象？怎么创建？是否单例？这是由编写该FactoryBean实现类的开发人员决定的）
        // 先尝试从缓存中拿
        Object object = getCacheObjectForFactoryBean(beanName);
        if (object == null) {
            getObjectFromFactoryBean((FactoryBean<?>) beanInstance, beanName);
        }
        return object;
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
