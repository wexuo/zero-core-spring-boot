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
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Getter
public class BaseController<T, ID, S extends BaseService<T, ID, ? extends BaseRepository<T, ID>>> {

    protected final S service;

    public BaseController(final S service) {
        this.service = service;
    }

    /**
     * 新增
     */
    public T add(@Validated @RequestBody final T user) {
        return this.service.save(user);
    }

    /**
     * 批量删除
     */
    public void delete(@RequestBody final List<ID> ids) throws ServiceRuntimeException {
        if (CollectionUtils.isEmpty(ids)) {
            throw new ServiceRuntimeException(HttpStatus.BAD_REQUEST, "ids is empty");
        }
        this.service.deleteByIds(ids);
    }

    /**
     * 更新
     */
    public T update(@Validated @RequestBody final T user) {
        return this.service.save(user);
    }

    /**
     * 查询全部
     */
    public <Q extends BaseQueryAccess> List<T> list(@RequestBody(required = false) final Q query) {
        return this.service.list(query);
    }

    /**
     * 条件+分页查询
     */
    public <Q extends BaseQueryAccess> PageResult<T> page(final Q query, final Pageable pageable) {
        final Page<T> page = this.service.page(query, pageable);
        return PageResult.convert(page);
    }

    /**
     * 查询详情
     */
    public T info(@PathVariable(value = "id") final ID id) throws ServiceRuntimeException {
        if (Objects.isNull(id)) {
            throw new ServiceRuntimeException(HttpStatus.BAD_REQUEST, "id is null");
        }
        return service.findById(id);
    }
}
