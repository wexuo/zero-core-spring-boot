/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.query;

import lombok.Data;

@Data
public class BaseQueryAccess implements QueryAccess {

    private String keyword;

    private Object start;

    private Object end;

    @Override
    public String getKeyword() {
        return keyword;
    }

    @Override
    public Object getStart() {
        return start;
    }

    @Override
    public Object getEnd() {
        return end;
    }
}
