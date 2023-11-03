package com.mizore.spring.test;

import com.mizore.spring.beans.factory.support.DefaultListableBeanFactory;
import com.mizore.spring.beans.factory.xml.XmlBeanDefinitionReader;
import com.mizore.spring.context.support.ClassPathXmlApplicationContext;
import com.mizore.spring.test.bean.UserService;
import com.mizore.spring.test.common.MyBeanFactoryPostProcessor;
import com.mizore.spring.test.common.MyBeanPostProcessor;
import org.junit.Test;

public class TestStep06 {
    @Test
    public void testWithoutBeanFactoryPostProcessorAndBeanPostProcessor01() throws Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");
        UserService userService = beanFactory.getBean("userService", UserService.class);
        userService.query();
    }
    @Test
    public void testBeanFactoryPostProcessorAndBeanPostProcessor01() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");
        // BeanDefinition加载完成 以及 bean实例化之前 修改 beanDefinition的属性值
        MyBeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        // bean实例化之后 修改bean属性信息
        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(beanPostProcessor);

        UserService userService = beanFactory.getBean("userService", UserService.class);
        userService.query();
    }

    @Test
    public void testBeanFactoryPostProcessorAndBeanPostProcessor02() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_2.xml");

        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.query();
    }

}
