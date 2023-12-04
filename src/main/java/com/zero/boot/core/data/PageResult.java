package com.zero.boot.core.data;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class PageResult<T> {

    private final Pagination pagination;

    private final List<T> list;

    public PageResult(final Page<T> page) {
        this.pagination = new Pagination(page);
        this.list = page.getContent();
    }

    public PageResult(final Page<?> page, final List<T> list) {
        this.pagination = new Pagination(page);
        this.list = list;
    }

    public static <T> PageResult<T> convert(final Page<T> page) {
        return new PageResult<>(page);
    }

    public static <T, R> PageResult<R> convert(final Page<T> page, final Function<T, R> function) {
        final List<R> list = page.getContent().stream().map(function).collect(Collectors.toList());
        return new PageResult<>(page, list);
    }

}
