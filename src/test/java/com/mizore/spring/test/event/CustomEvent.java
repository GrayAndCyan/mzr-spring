package com.mizore.spring.test.event;

import com.mizore.spring.context.event.ApplicationContextEvent;

// 自定义事件
public class CustomEvent extends ApplicationContextEvent {

    private int id;

    private String message;

    @Override
    public String toString() {
        return "CustomEvent{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }

    public CustomEvent(Object source, int id, String message) {
        super(source);
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
