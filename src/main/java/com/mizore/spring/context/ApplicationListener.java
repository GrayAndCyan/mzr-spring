package com.mizore.spring.context;

import java.util.EventListener;

/**
 *
 * @param <E> 这个监听者感兴趣的事件
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

    /**
     * 处理 application event
     * @param event 要去响应的事件
     */
    void onApplicationEvent(E event);

}
