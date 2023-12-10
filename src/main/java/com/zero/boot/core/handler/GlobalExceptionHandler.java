/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.handler;

import com.zero.boot.core.data.Result;
import com.zero.boot.core.exception.ServiceRuntimeException;
import javassist.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import javax.xml.bind.ValidationException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ServiceRuntimeException.class)
    public Result<?> handleException(final ServiceRuntimeException ex) {
        final HttpStatus status = ex.getStatus();
        log.error("handleException - ServiceRuntimeException exception", ex);
        return Result.error(status);
    }

    @ResponseBody
    @ExceptionHandler(value = {BindException.class, ValidationException.class, MethodArgumentNotValidException.class})
    public Result<?> handleValidationException(final Exception e) {
        if (e instanceof MethodArgumentNotValidException) {
            // BeanValidation exception
            final MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            log.error("handleValidationException - BeanValidation exception", ex);
        } else if (e instanceof ConstraintViolationException) {
            // BeanValidation GET simple param
            final ConstraintViolationException ex = (ConstraintViolationException) e;
            log.error("handleValidationException - ConstraintViolationException exception", ex);
        } else if (e instanceof BindException) {
            // BeanValidation GET object param
            final BindException ex = (BindException) e;
            log.error("handleValidationException - BindException exception", ex);
        }
        return Result.error(HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public Result<String> handleNotFoundException(final NotFoundException e) {
        return Result.error(HttpStatus.NOT_FOUND);
    }
}

