/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.util;

import org.springframework.context.ApplicationContext;

public class SpringUtils {

    private static ApplicationContext applicationContext;

    private SpringUtils() {
    }

    public static void setApplicationContext(final ApplicationContext context) {
        applicationContext = context;
    }

    public static <T> T getBean(final Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }
}
