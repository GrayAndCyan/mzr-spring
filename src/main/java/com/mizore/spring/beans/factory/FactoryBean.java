package com.mizore.spring.beans.factory;

import com.mizore.spring.beans.BeansException;

/**
 * 这个接口是提供给开发者的。开发者通过实现这个接口，可以自己编写bean的创建（包括bean实例化、设置属性、其他可能的初始化动作等）过程，
 * 并自行指明该bean类型和单例与否的信息。
 * @param <T>
 */
public interface FactoryBean<T> {

    T getObject() throws BeansException;

    Class<?> getObjectTYpe();

    /**
     * 开发者可以通过这个方法，告诉Spring框架，这个工厂bean创建的bean实例要不要搞成单例的。
     * 具体表现为，开发者编写自己的工厂bean类，实现这个接口，重写该方法时根据自己意愿设置返回值，告知spring框架。
     * @return
     */
    boolean isSingleton();
}
