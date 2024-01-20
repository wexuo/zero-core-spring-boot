/*
 * Copyright (c) 2023-2024 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code;


import com.zero.boot.code.data.PropertyWithType;
import com.zero.boot.code.data.TableData;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.type.Type;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TemplateBuilder {

    public static List<TableData> getTableData(final EntityManager manager) {
        final EntityManagerFactory factory = manager.getEntityManagerFactory();
        final MetamodelImpl metamodel = (MetamodelImpl) factory.getMetamodel();
        return metamodel.entityPersisters().values().stream()
                .filter(entry -> entry instanceof SingleTableEntityPersister)
                .map(entry -> (SingleTableEntityPersister) entry)
                .map(TemplateBuilder::getTableData).collect(Collectors.toList());
    }

    private static TableData getTableData(final SingleTableEntityPersister entity) {
        final String entityName = entity.getEntityName();
        final String tableName = entity.getTableName();

        final ClassMetadata metadata = entity.getClassMetadata();

        final String identifierName = metadata.getIdentifierPropertyName();
        final Type identifierType = metadata.getIdentifierType();
        final Class<?> identifierClazz = identifierType.getReturnedClass();

        final Class<?> clazz = metadata.getMappedClass();
        final List<Field> fields = FieldUtils.getAllFieldsList(clazz);

        final TableData tableData = new TableData();
        tableData.setTableName(tableName);
        tableData.setEntityName(entityName);
        tableData.setClazz(clazz);
        tableData.setIdentifierName(identifierName);
        tableData.setIdentifierType(identifierClazz);
        tableData.setProperties(getPropertyWithTypes(fields));
        return tableData;
    }

    private static List<PropertyWithType> getPropertyWithTypes(final List<Field> fields) {
        final List<PropertyWithType> properties = new ArrayList<>();
        for (final Field field : fields) {
            if (field.isAnnotationPresent(OneToOne.class)
                    || field.isAnnotationPresent(OneToMany.class)
                    || field.isAnnotationPresent(ManyToMany.class)) {
                continue;
            }
            properties.add(getProperty(field));
        }
        return properties;
    }

    private static PropertyWithType getProperty(final Field field) {
        final String name = field.getName();
        final PropertyWithType property = new PropertyWithType();
        property.setLabel(name);
        property.setProp(name);
        property.setType(field.getType().getSimpleName());
        if (field.isAnnotationPresent(Id.class)) {
            property.setNullable(false);
        }
        if (field.isAnnotationPresent(Column.class)) {
            final Column column = field.getAnnotation(Column.class);
            property.setNullable(column.nullable());
        } else {
            property.setNullable(true);
        }
        return property;
    }
}
