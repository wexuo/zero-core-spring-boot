/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code.data;

import com.zero.boot.core.query.QueryType;
import lombok.Data;

import java.io.Serializable;

@Data
public class ColumnData implements Serializable {
    private Boolean table;
    private Boolean upsert;
    private String component;
    private Boolean required;
    private QueryType queryType;
    private PropertyWithType property;
}
