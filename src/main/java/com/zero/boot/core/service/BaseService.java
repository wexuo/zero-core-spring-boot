/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.service;

import com.zero.boot.core.repository.BaseRepository;
import lombok.Getter;

@Getter
public class BaseService<T, ID, R extends BaseRepository<T, ID>> implements InsertService<T, ID, R>, DeleteService<T, ID, R>, QueryService<T, ID, R> {

    protected final R repository;

    public BaseService(final R repository) {
        this.repository = repository;
    }
}
