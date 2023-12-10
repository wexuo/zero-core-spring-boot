/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.controller;

import com.zero.boot.core.data.PageResult;
import com.zero.boot.core.exception.ServiceRuntimeException;
import com.zero.boot.core.query.BaseQueryAccess;
import com.zero.boot.core.repository.BaseRepository;
import com.zero.boot.core.service.BaseService;
import com.zero.boot.core.util.SpringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

public class BaseController<T, ID, Q extends BaseQueryAccess, S extends BaseService<T, ID, ? extends BaseRepository<T, ID>>> {

    private final S service;

    public BaseController() {
        this.service = (S) SpringUtils.getGenericBean(this.getClass(), 3);
    }

    /**
     * 新增
     */
    @PostMapping
    public T add(@Validated @RequestBody final T user) {
        return service.save(user);
    }

    /**
     * 批量删除
     */
    @DeleteMapping
    public void delete(@RequestBody final List<ID> ids) throws ServiceRuntimeException {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceRuntimeException(HttpStatus.BAD_REQUEST, "ids is empty");
        }
        service.deleteByIds(ids);
    }

    /**
     * 更新
     */
    @PutMapping
    public T update(@Validated @RequestBody final T user) {
        return service.save(user);
    }

    /**
     * 查询全部
     */
    @GetMapping(value = "/list")
    public List<T> list(@RequestBody(required = false) final Q query) {
        return service.list(query);
    }

    /**
     * 条件+分页查询
     */
    @GetMapping(value = "/page")
    public PageResult<T> page(final Q query, final Pageable pageable) {
        final Page<T> page = service.page(query, pageable);
        return PageResult.convert(page);
    }

    /**
     * 查询详情
     */
    @GetMapping(value = "/info/{id}")
    public T info(@PathVariable(value = "id") final ID id) throws ServiceRuntimeException {
        if (Objects.isNull(id)) {
            throw new ServiceRuntimeException(HttpStatus.BAD_REQUEST, "id is null");
        }
        return service.findById(id);
    }
}
