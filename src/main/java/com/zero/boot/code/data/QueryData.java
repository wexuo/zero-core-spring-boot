/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code.data;

import com.zero.boot.core.query.QueryType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryData extends PropertyWithType implements Serializable {

    private QueryType queryType;

    public QueryData(final PropertyWithType property, final QueryType queryType) {
        super(property);
        this.queryType = queryType;
    }
}
