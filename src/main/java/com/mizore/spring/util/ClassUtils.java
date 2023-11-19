package com.mizore.spring.util;

import cn.hutool.core.util.StrUtil;
import com.mizore.spring.context.ApplicationListener;
import net.bytebuddy.implementation.Implementation;
import net.sf.cglib.proxy.Proxy;

public class ClassUtils {

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassUtils.class.getClassLoader();
        }
        return cl;
    }

    public static boolean isProxySubClass(Class<?> targetClass) {
        return Proxy.isProxyClass(targetClass) || isByteBuddyProxyClass(targetClass);
    }
    private static boolean isByteBuddyProxyClass(Class<?> clazz) {
        while (clazz != null) {
            ClassLoader classLoader = clazz.getClassLoader();
            if (classLoader != null && classLoader.getClass().getName().equals("net.bytebuddy.dynamic.loading.ByteArrayClassLoader")) {
                return true;
            }
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static String getDefaultBeanName(Class<?> beanClass) {
        return StrUtil.lowerFirst(StrUtil.toCamelCase(beanClass.getSimpleName()));
    }
}
