package com.zero.boot.code;


import com.zero.boot.code.data.TableData;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metamodel.internal.MetamodelImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.type.Type;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateBuilder {

    public static List<TableData> getTableData(final EntityManager manager) {
        final EntityManagerFactory factory = manager.getEntityManagerFactory();
        final MetamodelImpl metamodel = (MetamodelImpl) factory.getMetamodel();

        final Map<String, EntityPersister> persisters = metamodel.entityPersisters();

        return persisters.values().stream().filter(entry -> entry instanceof SingleTableEntityPersister)
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

        final Map<String, Class<?>> propertyTypeMap = new HashMap<>();
        final String[] propertyNames = metadata.getPropertyNames();
        Arrays.stream(propertyNames).forEach(property -> {
            final Type type = metadata.getPropertyType(property);
            if (type.isAssociationType()) {
                return;
            }
            final Class<?> clazz = type.getReturnedClass();
            propertyTypeMap.put(property, clazz);
        });

        final TableData tableData = new TableData();
        tableData.setTableName(tableName);
        tableData.setEntityName(entityName);
        tableData.setIdentifierName(identifierName);
        tableData.setIdentifierType(identifierClazz);
        tableData.setPropertyTypeMap(propertyTypeMap);

        return tableData;
    }
}
