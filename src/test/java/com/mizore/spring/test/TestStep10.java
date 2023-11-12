package com.mizore.spring.test;

import com.mizore.spring.context.support.ClassPathXmlApplicationContext;
import com.mizore.spring.test.bean.UserService;
import com.mizore.spring.test.event.CustomEvent;
import com.mizore.spring.util.ClassUtils;
import org.junit.Test;


public class TestStep10 {

    @Test
    public void test10() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_10.xml");
        applicationContext.registerShutDownHook();
        applicationContext.publishEvent(new CustomEvent(applicationContext, 5201314, "我是自定义事件的消息"));
    }

    @Test
    public void testThis() {
        Student s = new Student();
        s.entre();
    }

    /**
     * 测试ClassUtil的isProxySubClass方法
     */
    @Test
    public void testIsProxySubClass() {
        // 1.初始化 BeanFactory
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_9.xml");
        applicationContext.registerShutDownHook();
        UserService userService = applicationContext.getBean("userService", UserService.class);
        System.out.println(ClassUtils.isProxySubClass(userService.getClass())); // true
        System.out.println(ClassUtils.isProxySubClass(UserService.class));  // false
    }

}

class People{

    protected void protectMethod() {
        privateMethod();
    }
    private void privateMethod() {
        System.out.println("do People#privateMethod");
        System.out.println(this.getClass().getName());
    }
}


class Student extends People{

    public void entre() {
        protectMethod();
    }

    private void privateMethod() {
        System.out.println("do People#privateMethod");
        System.out.println(this.getClass().getName());
    }
}