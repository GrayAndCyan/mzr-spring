package com.mizore.spring.context;

import com.mizore.spring.beans.BeansException;
import com.mizore.spring.beans.factory.aware.Aware;

public interface ApplicationContextAware  extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
