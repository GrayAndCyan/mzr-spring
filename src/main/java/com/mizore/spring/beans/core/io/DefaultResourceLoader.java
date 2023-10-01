package com.mizore.spring.beans.core.io;

import cn.hutool.core.lang.Assert;

import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader{

    private final static String CLASSPATH_URL_PREFIX = "classpath:";
    @Override
    public Resource getResource(String location) {
        Assert.notNull(location, "Location cannot be null");
        if (location.startsWith(CLASSPATH_URL_PREFIX)) {
            // 类路径资源
            return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
        }
        else {
            try {
                URL url = new URL(location);
                return new UrlResource(url);
            }  catch (MalformedURLException e) {
                // 格式错误异常则表示是文件系统资源
                return new FileSystemResource(location);
            }
        }
    }
}
