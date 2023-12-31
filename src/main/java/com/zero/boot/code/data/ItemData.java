/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ItemData extends Property implements Serializable {
    private String component;
    private Boolean required;

    public ItemData(final PropertyWithType property, final ColumnData column) {
        super(property);
        this.component = column.getComponent();
        this.required = column.getRequired();
    }
}
