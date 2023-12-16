/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.service;

import com.zero.boot.core.repository.BaseRepository;

import java.util.List;

public interface DeleteService<T, ID, R extends BaseRepository<T, ID>> extends Service<T, ID, R> {
    default void deleteByIds(final List<ID> ids) {
        this.getRepository().deleteAllById(ids);
    }
}
