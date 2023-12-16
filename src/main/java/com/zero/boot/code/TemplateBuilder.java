/*
 * Copyright (c) 2023 wexuo. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 */

package com.zero.boot.code;


import com.zero.boot.code.data.PropertyWithType;
import com.zero.boot.code.data.TableData;
import com.zero.boot.core.annotation.Comment;
import org.apache.commons.lang3.StringUtils;
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

        final List<PropertyWithType> properties = new ArrayList<>();

        for (final Field field : fields) {
            if (field.isAnnotationPresent(OneToOne.class)
                    || field.isAnnotationPresent(OneToMany.class)
                    || field.isAnnotationPresent(ManyToMany.class)) {
                continue;
            }
            final String name = field.getName();
            final PropertyWithType property = new PropertyWithType();
            String label = name;
            if (field.isAnnotationPresent(Comment.class)) {
                final Comment comment = field.getAnnotation(Comment.class);
                final String value = comment.value();
                if (StringUtils.isNotEmpty(value)) {
                    label = value;
                }
            }
            property.setLabel(label);
            property.setProp(name);
            property.setType(field.getType().getSimpleName());
            properties.add(property);
        }
        final TableData tableData = new TableData();
        tableData.setTableName(tableName);
        tableData.setEntityName(entityName);
        tableData.setClazz(clazz);
        tableData.setIdentifierName(identifierName);
        tableData.setIdentifierType(identifierClazz);
        tableData.setProperties(properties);
        return tableData;
    }
}
