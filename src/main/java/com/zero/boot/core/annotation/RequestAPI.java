package com.zero.boot.core.annotation;

import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;

public enum RequestAPI {
    ADD("add", RequestMethod.POST),
    DELETE("delete", RequestMethod.POST),
    UPDATE("update", RequestMethod.POST),
    PAGE("page", RequestMethod.POST),
    LIST("list", RequestMethod.POST),
    INFO("info", RequestMethod.GET);

    private final String method;

    private final RequestMethod requestMethod;

    RequestAPI(final String method, final RequestMethod requestMethod) {
        this.method = method;
        this.requestMethod = requestMethod;
    }

    public static RequestAPI valueOf(final Method method) {
        for (final RequestAPI api : values()) {
            if (api.getMethod().equals(method.getName())) {
                return api;
            }
        }
        return null;
    }

    public String getMethod() {
        return method;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }
}
