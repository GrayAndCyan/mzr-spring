package com.mizore.spring.beans.factory;

public interface DisposableBean {

    /**
     * 销毁操作
     * @throws Exception
     */
    void destroy() throws Exception;
}
