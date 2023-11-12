package com.mizore.spring.context;

import java.util.EventObject;

// 后续所有事件的类都要继承这个事件类
public abstract class ApplicationEvent extends EventObject {


    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
