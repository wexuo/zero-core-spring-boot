/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.example.query;

import com.zero.boot.core.query.BaseQueryAccess;
import com.zero.boot.core.query.annotation.Query;
import com.zero.boot.core.query.annotation.QueryBetween;
import com.zero.boot.core.query.annotation.QueryEqual;
import com.zero.boot.core.query.annotation.QueryLike;
import com.zero.boot.example.pojo.Example;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Query(Example.class)
@QueryLike(value = {"username"})
@QueryEqual(value = {"status"})
@QueryBetween(value = "createTime")
public class ExampleQuery extends BaseQueryAccess {
    private Byte status;
}
