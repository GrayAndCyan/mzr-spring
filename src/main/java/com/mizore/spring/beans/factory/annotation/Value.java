package com.mizore.spring.beans.factory.annotation;

import java.lang.annotation.*;

//@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {
    /**
     * 实际的值表达式  "#{SystemProperties.myProp}"
     */
    String value();
}
