package com.mizore.spring.beans.factory.aware;

/**
 * 标记超接口，指示 Bean 有资格通过回调样式的方法被 Spring 容器通知特定框架对象。
 * 实际的方法签名由各个子接口确定，但通常应仅包含一个接受单个参数的 void-return 方法。
 * .
 * .
 * 标志性接口，实现此接口可以被spring容器感知
 */
public interface Aware {
}
