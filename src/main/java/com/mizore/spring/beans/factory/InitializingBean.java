package com.mizore.spring.beans.factory;

public interface InitializingBean {

    /**
     * bean实例化并完成属性填充后调用
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}
