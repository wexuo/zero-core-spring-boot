/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code.config;

import com.zero.boot.code.data.ColumnData;
import com.zero.boot.code.data.PropertyWithType;
import com.zero.boot.code.data.TableData;
import com.zero.boot.core.query.QueryType;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GeneratorConfig implements Serializable {

    private String tableName;

    private String pack;

    private String path;

    private String prefix;

    private Boolean cover;

    private List<ColumnData> columns;

    public static GeneratorConfig covert(final TableData tableData) {
        // get path
        final String tableName = tableData.getTableName();
        final String path = getPath("t_", tableName);

        // get pack
        final String entityName = tableData.getEntityName();
        final String pack = getPack(entityName);

        // get columns
        final GeneratorConfig config = new GeneratorConfig();
        config.setTableName(tableName);
        config.setPack(pack);
        config.setPath(path);
        config.setPrefix("t_");
        config.setCover(true);
        config.setColumns(getColumns(tableData));
        return config;
    }

    private static String getPath(final String prefix, final String tableName) {
        if (StringUtils.isEmpty(prefix)) {
            return "/" + tableName.replaceAll("_", "/");
        }
        return "/" + tableName.replaceFirst(prefix, "").replaceAll("_", "/");
    }

    private static String getPack(final String entityName) {
        int idx = entityName.lastIndexOf(".pojo");
        if (idx == -1) {
            idx = entityName.lastIndexOf(".bean");
        }
        if (idx == -1) {
            idx = entityName.lastIndexOf(".data");
        }
        if (idx == -1) {
            idx = entityName.lastIndexOf(".");
        }
        return entityName.substring(0, idx);
    }

    private static List<ColumnData> getColumns(final TableData tableData) {
        final List<PropertyWithType> properties = tableData.getProperties();
        if (CollectionUtils.isEmpty(properties)) {
            return Collections.emptyList();
        }
        return properties.stream().map(property -> {
            final ColumnData columnData = new ColumnData();

            columnData.setProperty(property);
            // set default
            columnData.setUpsert(true);
            columnData.setTable(true);
            columnData.setRequired(true);
            columnData.setComponent("el-input");
            if (property.getProp().contains("name")) {
                columnData.setQueryType(QueryType.LIKE);
                columnData.setComponent("el-input");
            }
            if (property.getProp().contains("Time")) {
                columnData.setQueryType(QueryType.BETWEEN);
                columnData.setComponent("el-date-picker");
                columnData.setRequired(false);
            }
            if (property.getProp().contains("status")) {
                columnData.setQueryType(QueryType.EQUAL);
                columnData.setComponent("el-select");
            }
            if (property.getProp().contains("img") || property.getProp().contains("avatar") || property.getProp().contains("pic")) {
                columnData.setComponent("cl-upload");
            }
            return columnData;
        }).collect(Collectors.toList());
    }
}
