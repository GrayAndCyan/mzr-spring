package com.mizore.spring.test;

import com.mizore.spring.context.support.ClassPathXmlApplicationContext;
import com.mizore.spring.test.bean.IOrderService;
import org.junit.Test;

public class TestStep12Proxy2Bean {


    @Test
    public void testProxy2Bean() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_12.xml");
        IOrderService orderService = applicationContext.getBean("orderService", IOrderService.class);
        System.out.println(orderService.create("1234") + "\n");
        System.out.println(orderService.getUserService());
    }
}
