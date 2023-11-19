package com.mizore.spring.test;

import cn.hutool.core.util.StrUtil;
import com.mizore.spring.context.support.ClassPathXmlApplicationContext;
import com.mizore.spring.test.bean.OrderService;
import com.mizore.spring.test.bean.UserService;
import com.mizore.spring.util.ClassUtils;
import org.junit.Test;

public class TestStep13AnnotationScan {


    @Test
    public void testAnnotation() {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring_13_property.xml");
        UserService myUserService = applicationContext.getBean("myUserService", UserService.class);
        System.out.println(myUserService);
        System.out.println(applicationContext.getBean("orderService", OrderService.class));
    }
    @Test
    public void testCamelCase() {
        /**
         * 有下划线： 全部小写，下划线后大写，去掉下划线
         * 无下划线： 神魔都不做
         * toCamelCase(name, '_')
         */
        System.out.println(StrUtil.toCamelCase(this.getClass().getName())); // com.mizore.spring.test.TestStep13AnnotationScan
        System.out.println(StrUtil.toCamelCase("UserService")); // UserService
        System.out.println(StrUtil.toCamelCase("IUserSer_vice"));   // iuserserVice
        System.out.println(StrUtil.toCamelCase("userservice")); // userservice
        System.out.println(StrUtil.toCamelCase("user_serVice"));    // userService
        System.out.println(StrUtil.toCamelCase("_userService"));    // UserService

        System.out.println(
                this.getClass().getSimpleName() + "\n" +
                this.getClass().getCanonicalName() + "\n" +
                this.getClass().getTypeName() + "\n"
        );
    }

    @Test
    public void testToLowerCamel() {
        System.out.println(ClassUtils.getDefaultBeanName(this.getClass()));
    }
}
