package com.zero.boot.core.controller;

import com.zero.boot.core.data.PageResult;
import com.zero.boot.core.data.Result;
import com.zero.boot.core.query.BaseQueryAccess;
import com.zero.boot.core.repository.BaseRepository;
import com.zero.boot.core.service.BaseService;
import com.zero.boot.core.util.SpringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

public class BaseController<T, ID, S extends BaseService<T, ID, ? extends BaseRepository<T, ID>>> {

    private final S service;

    public BaseController() {
        this.service = (S) SpringUtils.getGenericBean(this.getClass(), 2);
    }

    public Result<T> add(@RequestBody final T user) {
        return Result.ok(service.save(user));
    }

    public Result<Void> delete(@RequestBody final List<ID> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Result.error(HttpStatus.BAD_REQUEST, "");
        }
        service.deleteByIds(ids);
        return Result.ok();
    }

    public Result<T> update(@RequestBody final T user) {
        return Result.ok(service.save(user));
    }

    public <Q extends BaseQueryAccess> Result<List<T>> list(@RequestBody(required = false) final Q query) {
        return Result.ok(service.list());
    }

    public <Q extends BaseQueryAccess> Result<PageResult<T>> page(final Q query, final Pageable pageable) {
        final Page<T> page = service.page(query, pageable);
        return Result.ok(PageResult.convert(page));
    }

    public Result<T> info(final ID id) {
        if (Objects.isNull(id)) {
            return Result.error("The given id must not be null.");
        }
        return Result.ok(service.findById(id));
    }
}
