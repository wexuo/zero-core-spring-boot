package com.zero.boot.core.util;

import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;

import java.util.Objects;

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

    public static <T> Object getGenericBean(final Class<T> requiredType, final int idx) {
        final ResolvableType resolvableType = ResolvableType.forClass(requiredType);
        final ResolvableType generic = resolvableType.getSuperType().getGeneric(idx);
        final Class<?> clazz = generic.getRawClass();
        if (Objects.isNull(clazz)) {
            return null;
        }
        return getBean(generic.getRawClass());
    }
}
