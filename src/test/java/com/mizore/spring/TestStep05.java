package com.mizore.spring;


import cn.hutool.core.io.IoUtil;
import com.mizore.spring.beans.core.io.DefaultResourceLoader;
import com.mizore.spring.beans.core.io.Resource;
import com.mizore.spring.beans.factory.support.DefaultListableBeanFactory;
import com.mizore.spring.beans.factory.xml.XmlBeanDefinitionReader;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class TestStep05{

    private DefaultResourceLoader resourceLoader;
    @Before
    public void init() {
        resourceLoader = new DefaultResourceLoader();
    }
    @Test
    public void test_classpath() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:important.properties");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
    @Test
    public void test_file() throws IOException {
        // 测试类写相对路径以在工程目录为基准
        Resource resource = resourceLoader.getResource("src/test/resources/spring.xml");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }
    @Test
    public void test_url() throws IOException {
        Resource resource = resourceLoader.getResource("https://github.com/GrayAndCyan/mzr-spring/blob/master/pom.xml");
        InputStream inputStream = resource.getInputStream();
        String content = IoUtil.readUtf8(inputStream);
        System.out.println(content);
    }

    @Test
    public void testGetBean() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions("classpath:spring.xml");
        UserService userService = (UserService) beanFactory.getBean("userService");
        userService.query();
    }
}
