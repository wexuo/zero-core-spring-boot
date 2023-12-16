/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code.data;

import com.zero.boot.code.config.GeneratorConfig;
import com.zero.boot.core.query.QueryType;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
public class TemplateData implements Serializable {
    private Class<?> clazz;
    private String pack;
    private String entityName;
    private String entityPackage;
    private String entityCamelCaseName;
    private String primaryKeyType;

    private List<QueryData> equalQuerys;

    private List<String> likes;

    private String between;

    private String path;
    private List<ItemData> items;
    private List<Property> columns;

    public static TemplateData covert(final TableData tableData, GeneratorConfig config) {
        if (Objects.isNull(config)) {
            config = GeneratorConfig.covert(tableData);
        }
        final TemplateData templateData = new TemplateData();

        String entityName = tableData.getEntityName();

        templateData.setEntityPackage(entityName);

        entityName = entityName.substring(entityName.lastIndexOf(".") + 1);

        final String perfix = config.getPrefix();

        final char c = entityName.replaceFirst(perfix, "").charAt(0);
        final char l = Character.toLowerCase(c);

        final String entityCamelCaseName = entityName.replaceFirst(Character.toString(c), Character.toString(l));

        final Class<?> clazz = tableData.getIdentifierType();
        final String primaryKeyType = clazz.getSimpleName();

        final List<ColumnData> columns = config.getColumns();

        templateData.setPack(config.getPack());
        templateData.setEntityName(entityName);
        templateData.setEntityCamelCaseName(entityCamelCaseName);
        templateData.setPrimaryKeyType(primaryKeyType);

        final List<QueryData> querys = getQueryData(columns);
        final List<String> likes = querys.stream()
                .filter(query -> QueryType.LIKE.equals(query.getQueryType()))
                .map(QueryData::getProp)
                .collect(Collectors.toList());

        final List<QueryData> equals = querys.stream()
                .filter(query -> QueryType.EQUAL.equals(query.getQueryType()))
                .collect(Collectors.toList());

        final Optional<String> optional = querys.stream()
                .filter(query -> QueryType.BETWEEN.equals(query.getQueryType()))
                .map(QueryData::getProp).findFirst();
        if (CollectionUtils.isNotEmpty(likes)) {
            templateData.setLikes(likes);
        }
        if (CollectionUtils.isNotEmpty(equals)) {
            templateData.setEqualQuerys(equals);
        }
        optional.ifPresent(templateData::setBetween);
        if (CollectionUtils.isNotEmpty(columns)) {
            templateData.setItems(getItemData(columns));
            templateData.setColumns(getColumns(columns));
        }

        templateData.setPath(config.getPath());
        templateData.setClazz(tableData.getClazz());

        return templateData;
    }

    private static List<QueryData> getQueryData(final List<ColumnData> columns) {
        return columns.stream().filter(column -> Objects.nonNull(column.getQueryType()))
                .map(column -> new QueryData(column.getProperty(), column.getQueryType())).collect(Collectors.toList());
    }

    private static List<ItemData> getItemData(final List<ColumnData> columns) {
        return columns.stream().filter(column -> Objects.nonNull(column) && column.getUpsert())
                .map(column -> new ItemData(column.getProperty(), column)).collect(Collectors.toList());
    }

    private static List<Property> getColumns(final List<ColumnData> columns) {
        return columns.stream().filter(column -> Objects.nonNull(column) && column.getTable())
                .map(ColumnData::getProperty).collect(Collectors.toList());
    }
}
