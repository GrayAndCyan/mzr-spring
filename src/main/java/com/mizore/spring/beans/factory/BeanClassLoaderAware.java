package com.mizore.spring.beans.factory;

import com.mizore.spring.beans.factory.aware.Aware;

public interface BeanClassLoaderAware extends Aware {

    void setBeanClassLoader(ClassLoader classLoader);
}
