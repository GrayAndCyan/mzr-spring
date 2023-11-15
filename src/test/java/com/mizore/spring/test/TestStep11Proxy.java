package com.mizore.spring.test;

import com.mizore.spring.aop.AdvisedSupport;
import com.mizore.spring.aop.TargetSource;
import com.mizore.spring.aop.aspectj.AspectJExpressionPointcut;
import com.mizore.spring.aop.MethodMatcher;
import com.mizore.spring.aop.framework.Cglib2AopProxy;
import com.mizore.spring.aop.framework.JdkDynamicAopProxy;
import com.mizore.spring.aop.framework.ReflectiveMethodInvocation;
import com.mizore.spring.test.bean.IOrderService;
import com.mizore.spring.test.bean.IUserService;
import com.mizore.spring.test.bean.OrderService;
import com.mizore.spring.test.bean.UserService;
import com.mizore.spring.test.interceptor.OrderServiceInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestStep11Proxy {

    @Test
    public void testJdkAndCglibAop() {
        // 目标对象
        IOrderService orderService = new OrderService();

        // 包装advised
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setMethodInterceptor(new OrderServiceInterceptor());
        advisedSupport.setTargetSource(new TargetSource(orderService));
        advisedSupport.setMethodMatcher(new AspectJExpressionPointcut(
                "execution(* com.mizore.spring.test.bean.IOrderService.*(..))"
        ));

        IOrderService proxyJdk = (IOrderService)new JdkDynamicAopProxy(advisedSupport).getProxy();
        System.out.println(proxyJdk.create("12345"));

        // cglib jdk17+ 不管用了
/*        IOrderService proxyCglib = (IOrderService)new Cglib2AopProxy(advisedSupport).getProxy();
        System.out.println(proxyCglib.query());*/
    }


    @Test
    public void testProxy() {
        // 目标对象
        Object userService = new UserService();
        // 代理对象
        IUserService proxy = (IUserService)Proxy
                .newProxyInstance(
                        Thread.currentThread().getContextClassLoader(),
                        userService.getClass().getInterfaces(),
                        new InvocationHandler() {

            // 方法匹配器
            MethodMatcher methodMatcher = new AspectJExpressionPointcut("execution(* com.mizore.spring.test.bean.IUserService.*(..))");
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (methodMatcher.matches(method, userService.getClass())) {
                    MethodInterceptor methodInterceptor = methodInvocation -> {
                        long begin = System.currentTimeMillis();
                        try {
                            return methodInvocation.proceed();
                        } finally {
                            System.out.println("Begin by AOP");
                            System.out.println("方法名： " + method.getName());
                            long end = System.currentTimeMillis();
                            System.out.println("method spend /ms: " + (end - begin));
                        }
                    };
                    return methodInterceptor.invoke(new ReflectiveMethodInvocation(userService, method, args));
                }
                // 不匹配
                return method.invoke(proxy, args);
            }
        });
        // 使用代理对象
        proxy.query();
    }

    @Test
    public void  testMatch() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
                "execution(* com.mizore.spring.test.bean.UserService.*(..))"
        );
        Class<UserService> clazz = UserService.class;
        Method method = clazz.getDeclaredMethod("query");
        System.out.println(pointcut.matches(clazz));
        System.out.println(pointcut.matches(method, clazz));
    }
}
