package com.mizore.spring.context.event;

import com.mizore.spring.context.ApplicationContext;
import com.mizore.spring.context.ApplicationEvent;

// ApplicationContextEvent 是定义事件的类，所有的事件包括关闭、刷新，以及用户自己实现的事件，都需要继承这个类。
public class ApplicationContextEvent extends ApplicationEvent {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationContextEvent(Object source) {
        super(source);
    }

    /**
     * Get the <code>ApplicationContext</code> that the event was raised for
     * 获取引发事件的应用上下文
     * @return
     */
    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
