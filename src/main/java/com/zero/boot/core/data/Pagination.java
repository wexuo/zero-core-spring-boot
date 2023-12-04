package com.zero.boot.core.data;

import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class Pagination {
    private final int page;
    private final int size;
    private final long total;

    public Pagination(final Page page) {
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.total = page.getTotalElements();
    }

}
