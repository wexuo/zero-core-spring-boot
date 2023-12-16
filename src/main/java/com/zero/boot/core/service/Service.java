/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.service;

import com.zero.boot.core.repository.BaseRepository;

public interface Service<T, ID, R extends BaseRepository<T, ID>> {
    R getRepository();
}
