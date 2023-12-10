/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Property implements Serializable {

    private String prop;

    private String label;

    public Property(final Property property) {
        this.prop = property.getProp();
        this.label = property.getLabel();
    }
}
