package com.mizore.spring.aop.framework;

/**
 * 由于具体的实现代理的方式有jdk动态代理，也有cglib动态字节码增强技术代理等方案，所以定义接口可以更加方便管理实现类
 */
public interface AopProxy {

    Object getProxy();
}
