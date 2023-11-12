package com.mizore.spring.context.event;

import com.mizore.spring.context.ApplicationEvent;
import com.mizore.spring.context.ApplicationListener;

public interface ApplicationEventMulticaster {

    /**
     * 添加listener 被通知所有事件
     * @param listener
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 从通知列表中移除一个listener
     * @param listener
     */
    void removeApplicationListener(ApplicationListener<?> listener);

    /**
     * 将指定的applicationEvent广播给相应的监听者
     * @param event
     */
    void multicastEvent(ApplicationEvent event);
}
