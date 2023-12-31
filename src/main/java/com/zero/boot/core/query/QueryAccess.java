/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.core.query;

public interface QueryAccess {
    String getKeyword();

    Object getStart();

    Object getEnd();
}
