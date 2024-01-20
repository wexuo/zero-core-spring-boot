/*
 * Copyright (c) 2023-2024 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.data;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Result<T> {

    private final Integer code;

    private final String message;

    private final T data;

    public Result(final Integer code, final String msg, final T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public static <T> Result<T> ok() {
        return new Result<T>(HttpStatus.OK.value(), "", null);
    }

    public static <T> Result<T> ok(final T data) {
        return new Result<T>(HttpStatus.OK.value(), "", data);
    }

    public static <T> Result<T> error(final HttpStatus httpStatus) {
        return new Result<>(httpStatus.value(), httpStatus.getReasonPhrase(), null);
    }

    public static <T> Result<T> error(final String message) {
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    public static <T> Result<T> error(final HttpStatus status, final String message) {
        return new Result<>(status.value(), message, null);
    }
}