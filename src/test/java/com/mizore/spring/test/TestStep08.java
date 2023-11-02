package com.mizore.spring.test;

import com.mizore.spring.context.support.ClassPathXmlApplicationContext;
import com.mizore.spring.test.bean.User;
import com.mizore.spring.test.bean.UserService;
import org.junit.Test;

// aware
public class TestStep08 {

    @Test
    public void test08() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_2.xml");
        applicationContext.registerShutDownHook();
        UserService userService = applicationContext.getBean("userService", UserService.class);
        userService.query();
    }

    @Test
    public void t() {
        System.out.println(new User() instanceof User); // t
        System.out.println(Object.class.isAssignableFrom(User.class));  // t
    }
}
