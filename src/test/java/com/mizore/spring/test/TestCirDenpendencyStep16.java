package com.mizore.spring.test;

import com.mizore.spring.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

public class TestCirDenpendencyStep16 {

    @Test
    public void testCacheSolveCD() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_16.xml");
    }
}
