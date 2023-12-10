/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServiceRuntimeException extends Exception {

    private static final long serialVersionUID = 1L;
    private final HttpStatus status;

    public ServiceRuntimeException(final HttpStatus status) {
        super();
        this.status = status;
    }

    public ServiceRuntimeException(final HttpStatus status, final String message) {
        super(message);
        this.status = status;
    }

}
