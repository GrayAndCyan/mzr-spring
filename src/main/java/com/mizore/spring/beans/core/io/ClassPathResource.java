package com.mizore.spring.beans.core.io;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ClassPathResource implements Resource{

    private final String path;

    private ClassLoader classLoader;

    // 属性避免空值处理
    public ClassPathResource(String path) {
        this(path, null);
    }

    public ClassPathResource(String path, ClassLoader classLoader) {
        Assert.notNull(path, "The path can not be null");
        this.path = path;
        this.classLoader = classLoader != null ? classLoader : ClassUtil.getClassLoader();
    }

    @Override
    public InputStream getInputStream() throws FileNotFoundException {
        InputStream is = classLoader.getResourceAsStream(path);
        if (is == null)
            throw new FileNotFoundException(this.path + "cannot be opened becauser it does not exist");
        return is;
    }
}
