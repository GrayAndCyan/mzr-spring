package com.mizore.spring.test;

import com.mizore.spring.context.support.ClassPathXmlApplicationContext;
import com.mizore.spring.test.bean.UserService;
import org.junit.Test;

public class TestStep07 {

    @Test
    public void testHock() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                () -> {
                    System.out.println(Thread.currentThread().getName() + ": bye~ mizore!! ");
                }
        ));
    }

    @Test
    public void testInitAndDestroyMethod() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_2.xml");
        applicationContext.registerShutDownHook();
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.query();
    }
}
