package com.mizore.spring.test;

import com.mizore.spring.context.support.ClassPathXmlApplicationContext;
import com.mizore.spring.test.bean.UserService;
import org.junit.Test;

public class TestStep14FieldAnnotation {

    @Test
    public void test() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_14.xml");
        UserService myUserService = applicationContext.getBean("myUserService", UserService.class);
        System.out.println(myUserService);
    }
}
