package com.zero.boot.core.query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataQuery {

    String property() default "";

    Type type() default Type.EQUAL;

    enum Type {
        EQUAL, LIKE, IN, NOT_IN, NOT_EQUAL, BETWEEN
    }
}
