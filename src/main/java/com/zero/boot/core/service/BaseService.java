/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.service;

import com.zero.boot.core.query.QueryAccess;
import com.zero.boot.core.query.QueryHelper;
import com.zero.boot.core.repository.BaseRepository;
import com.zero.boot.core.util.SpringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;

public class BaseService<T, ID, R extends BaseRepository<T, ID>> {

    private final R repository;

    public BaseService() {
        this.repository = (R) SpringUtils.getGenericBean(this.getClass(), 2);
    }

    public T findById(final ID id) {
        return repository.findById(id).orElse(null);
    }

    public T save(final T entity) {
        return repository.save(entity);
    }

    public void deleteByIds(final List<ID> ids) {
        repository.deleteAllByIdInBatch(ids);
    }

    public <Q extends QueryAccess> List<T> list(final Q query) {
        if (Objects.isNull(query)) {
            return repository.findAll();
        }
        return repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelper.getPredicate(root, query, criteriaBuilder));
    }

    public <Q extends QueryAccess> Page<T> page(final Q query, final Pageable pageable) {
        if (Objects.isNull(query)) {
            return repository.findAll(pageable);
        }
        return repository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelper.getPredicate(root, query, criteriaBuilder), pageable);
    }
}
