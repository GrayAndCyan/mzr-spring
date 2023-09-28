package com.mizore.spring.beans.factory.config;

/**
 * Bean引用类型，bean被其他类依赖时，这个属性的类型，指向被依赖bean的name
 */
public class BeanReference {
    private String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
