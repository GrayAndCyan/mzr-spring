package com.mizore.spring.beans.factory;

public class PropertyValue {
    private String name;
    private Object value;

    public PropertyValue(String name, Object value) {
        this.value = value;
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
