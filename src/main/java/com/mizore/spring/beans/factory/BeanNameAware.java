package com.mizore.spring.beans.factory;

import com.mizore.spring.beans.factory.aware.Aware;

public interface BeanNameAware extends Aware {

    void setBeanName(String name);
}
