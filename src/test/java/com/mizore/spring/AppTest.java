package com.mizore.spring;

import com.mizore.spring.beans.factory.config.BeanDefinition;
import com.mizore.spring.beans.factory.support.DefaultListableBeanFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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
}
