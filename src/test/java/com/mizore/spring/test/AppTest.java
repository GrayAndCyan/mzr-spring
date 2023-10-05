package com.mizore.spring.test;

import cn.hutool.core.bean.BeanUtil;
import com.mizore.spring.beans.PropertyValue;
import com.mizore.spring.beans.PropertyValues;
import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.config.BeanReference;
import com.mizore.spring.beans.factory.support.DefaultListableBeanFactory;
import com.mizore.spring.test.bean.User;
import com.mizore.spring.test.bean.UserDao;
import com.mizore.spring.test.bean.UserService;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registryBeanDefinition("UserService",new BeanDefinition(UserService.class));
        UserService bean = (UserService) beanFactory.getBean("UserService");
        bean.query();
        UserService beanSingleton = (UserService) beanFactory.getBean("UserService");
        beanSingleton.query();

    }

    public void testdt() {
        Object us = new UserService();
        System.out.println(us.getClass().getName());
    }

    public void testdt2() {
        Set<Class> set = new HashSet<>();
        set.add(Object.class);
        Object us = new UserService();
        System.out.println(set.remove(us.getClass()));
        int n = 42;
        System.out.println(int.class.isInstance(n));
        System.out.println(Integer.class.isInstance(n));
        for (Constructor<?> constructor : User.class.getConstructors()) {
            System.out.println(constructor);
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Arrays.stream(parameterTypes).forEach(System.out::println);
            for (Class<?> parameterType : parameterTypes) {
                System.out.println(parameterType.isInstance(42));
            }
        }

    }

    public void testStep03_0() {
        String name = "user";
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registryBeanDefinition(name, new BeanDefinition(User.class));
        Object bean = beanFactory.getBean(name);
        System.out.println(bean.toString());
    }
    public void testStep03_1() {
        String name = "user";
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registryBeanDefinition(name, new BeanDefinition(User.class));
        Object bean = beanFactory.getBean(name, "hello",10);
        System.out.println(bean.toString());
    }

    public void testStep04_1() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 注册userDao bean
        beanFactory.registryBeanDefinition("userDao" ,new BeanDefinition(UserDao.class, null));
        // 注册userService并属性注入
        PropertyValues propertyValues = new PropertyValues();
        propertyValues.addPropertyValue(new PropertyValue("name","mizore"));
        propertyValues.addPropertyValue(new PropertyValue("userDao",new BeanReference("userDao")));
        beanFactory.registryBeanDefinition("userService" ,new BeanDefinition(UserService.class, propertyValues));
        // 获取bean
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.query();
        UserService userService2 = (UserService) beanFactory.getBean("userService");
//        System.out.println(userService2.userDao.queryUserName("10001"));
//        System.out.println(userService2.name);
        System.out.println(userService2.equals(userService));
    }

    public void testSetField() {
        User user = new User();
        BeanUtil.setFieldValue(user, "username", "mizore");
        System.out.println(user);
    }



}
