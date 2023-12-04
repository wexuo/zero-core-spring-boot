package com.zero.boot.code.data;

import com.zero.boot.code.config.GeneratorConfig;
import lombok.Data;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class TemplateData implements Serializable {
    private String pack;
    private String entityName;
    private String entityCamelCaseName;
    private String primaryKeyType;

    private List<QueryData> querys;

    private List<PropertyData> betweens;

    private String path;
    private List<ItemData> items;
    private List<Property> columns;

    public static TemplateData covert(final TableData tableData, GeneratorConfig config) {
        if (Objects.isNull(config)) {
            config = GeneratorConfig.covert(tableData);
        }
        String entityName = tableData.getEntityName();

        entityName = entityName.substring(entityName.lastIndexOf(".") + 1);

        final String perfix = config.getPrefix();

        final char c = entityName.replaceFirst(perfix, "").charAt(0);
        final char l = Character.toLowerCase(c);

        final String entityCamelCaseName = entityName.replaceFirst(Character.toString(c), Character.toString(l));

        final Class<?> clazz = tableData.getIdentifierType();
        final String primaryKeyType = clazz.getSimpleName();

        final List<ColumnData> columns = config.getColumns();


        final TemplateData templateData = new TemplateData();

        templateData.setPack(config.getPack());
        templateData.setEntityName(entityName);
        templateData.setEntityCamelCaseName(entityCamelCaseName);
        templateData.setPrimaryKeyType(primaryKeyType);

        templateData.setQuerys(getQueryData(columns));
        templateData.setBetweens(getBetween(columns));
        templateData.setItems(getItemData(columns));
        templateData.setColumns(getColumns(columns));

        templateData.setPath(config.getPath());


        return templateData;
    }

    private static List<QueryData> getQueryData(final List<ColumnData> columns) {
        return columns.stream().filter(column -> Objects.nonNull(column.getQueryType()) && !"BETWEEN".equals(column.getQueryType()))
                .map(column -> {
                    final QueryData queryData = new QueryData();
                    final PropertyData propertyData = column.getPropertyData();
                    queryData.setProperty(propertyData.getProperty());
                    queryData.setPropertyType(propertyData.getPropertyType());
                    queryData.setQueryType(column.getQueryType());
                    return queryData;
                }).sorted(Comparator.comparing(Property::getProperty)).collect(Collectors.toList());
    }

    private static List<PropertyData> getBetween(final List<ColumnData> columns) {
        return columns.stream().filter(column -> Objects.nonNull(column.getQueryType()) && "BETWEEN".equals(column.getQueryType()))
                .map(ColumnData::getPropertyData).sorted(Comparator.comparing(Property::getProperty)).collect(Collectors.toList());
    }

    private static List<ItemData> getItemData(final List<ColumnData> columns) {
        return columns.stream().filter(column -> Objects.nonNull(column) && column.getForm())
                .map(column -> {
                    final ItemData itemData = new ItemData();

                    final PropertyData propertyData = column.getPropertyData();

                    itemData.setComponent(column.getFormType());
                    itemData.setRequired(column.getRequired());
                    itemData.setLabel(propertyData.getProperty());
                    itemData.setProperty(propertyData.getProperty());

                    return itemData;
                }).sorted(Comparator.comparing(Property::getProperty)).collect(Collectors.toList());
    }

    private static List<Property> getColumns(final List<ColumnData> columns) {
        return columns.stream().filter(column -> Objects.nonNull(column) && column.getList())
                .map(ColumnData::getPropertyData).sorted(Comparator.comparing(Property::getProperty)).collect(Collectors.toList());
    }
}
