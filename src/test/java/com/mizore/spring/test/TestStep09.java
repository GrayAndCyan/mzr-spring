package com.mizore.spring.test;

import com.mizore.spring.context.support.ClassPathXmlApplicationContext;
import com.mizore.spring.test.bean.IUserDao;
import com.mizore.spring.test.bean.ProxyBeanFactory;
import com.mizore.spring.test.bean.UserService;
import org.junit.Test;

public class TestStep09 {

    @Test
    public void test_prototype() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_9.xml");
        applicationContext.registerShutDownHook();
        // 2. 获取 Bean 对象调用方法
        UserService userService01 = applicationContext.getBean("userService", UserService.class);
        UserService userService02 = applicationContext.getBean("userService", UserService.class);
        // 3. 配置 scope="prototype/singleton"
        System.out.println(userService01);
        System.out.println(userService02);
    }

    @Test
    public void test_factory_bean() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_9.xml");
        applicationContext.registerShutDownHook();
        // 2. 调用代理方法
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.query();
        System.out.println(applicationContext.getBean("proxyUserDao", ProxyBeanFactory.class));
    }

}
