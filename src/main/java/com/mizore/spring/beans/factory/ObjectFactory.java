package com.mizore.spring.beans.factory;

import com.mizore.spring.beans.BeansException;

public interface ObjectFactory<T> {

    T getObject() throws BeansException;
}
