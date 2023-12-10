/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code.data;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 数据表实体类
 */
@Data
public class TableData implements Serializable {
    /**
     * 数据表名称
     */
    private String tableName;
    /**
     * 实体类名称
     */
    private String entityName;
    /**
     * 实体类名
     */
    private Class<?> clazz;
    /**
     * 主键名
     */
    private String identifierName;
    /**
     * 主键类型
     */
    private Class<?> identifierType;

    private List<PropertyWithType> properties;
}
