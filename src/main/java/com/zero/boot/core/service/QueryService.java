/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.service;

import com.zero.boot.core.query.QueryAccess;
import com.zero.boot.core.query.QueryHelper;
import com.zero.boot.core.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

public interface QueryService<T, ID, R extends BaseRepository<T, ID>> extends Service<T, ID, R> {

    default T findById(final ID id) {
        return this.getRepository().findById(id).orElse(null);
    }

    default <Q extends QueryAccess> List<T> list(final Q query) {
        if (Objects.isNull(query)) {
            return this.getRepository().findAll();
        }
        return this.getRepository().findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelper.getPredicate(root, query, criteriaBuilder));
    }

    default <Q extends QueryAccess> Page<T> page(final Q query, final Pageable pageable) {
        if (Objects.isNull(query)) {
            return this.getRepository().findAll(pageable);
        }
        return this.getRepository().findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelper.getPredicate(root, query, criteriaBuilder), pageable);
    }
}
