package com.mizore.spring.aop;

// 类过滤器，用于找到pointcut适用的接口和类
public interface ClassFilter {

    /**
     * 判断给定的类与接口是否匹配pointcut
     * @param clazz 候选的类或接口
     * @return 匹配结果
     */
    boolean matches(Class<?> clazz);
}
