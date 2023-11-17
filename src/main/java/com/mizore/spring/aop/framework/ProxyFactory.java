package com.mizore.spring.aop.framework;

import com.mizore.spring.aop.AdvisedSupport;

// 代理工厂主要解决jdk和cglib两种代理的选择问题，有了代理工厂就可以按照不同的创建需求进行控制
public class ProxyFactory {

    private AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy() {
        if (advisedSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(advisedSupport);
        }
        return new JdkDynamicAopProxy(advisedSupport);
    }
}
