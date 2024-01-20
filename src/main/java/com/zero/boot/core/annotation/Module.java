/*
 * Copyright (c) 2024 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Module {
    String name();

    Class<?> entity() default Void.class;

    String desc() default "";
}
