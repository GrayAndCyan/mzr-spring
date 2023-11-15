package com.mizore.spring.test.bean;

import com.mizore.spring.beans.factory.FactoryBean;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class ProxyBeanFactory implements FactoryBean<IUserDao> {

    @Override
    public IUserDao getObject() {

        // 使用jdk动态代理在运行时产生实现IUserDao接口的实例


        return (IUserDao) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{IUserDao.class},
                (proxy, method, args) -> {
                    Map<String, String> hashMap = new HashMap<>();
                    hashMap.put("10001", "mizore");
                    hashMap.put("10002", "八杯水");
                    hashMap.put("10003", "阿毛");
//                    return "你被代理了 " + method.getName() + "：" + hashMap.get(args[0].toString());
                    return "你被代理了 " + method.getName();
                });
    }

    @Override
    public Class<?> getObjectTYpe() {
        return IUserDao.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
