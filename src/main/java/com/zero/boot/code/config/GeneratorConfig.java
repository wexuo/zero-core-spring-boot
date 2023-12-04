package com.zero.boot.code.config;

import com.zero.boot.code.data.ColumnData;
import com.zero.boot.code.data.PropertyData;
import com.zero.boot.code.data.TableData;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
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
        final Map<String, Class<?>> propertyTypeMap = tableData.getPropertyTypeMap();
        final List<ColumnData> columns = getColumns(propertyTypeMap);
        final GeneratorConfig config = new GeneratorConfig();
        config.setTableName(tableName);
        config.setPack(pack);
        config.setPath(path);
        config.setPrefix("t_");
        config.setCover(true);
        config.setColumns(columns);
        return config;
    }

    private static String getPath(final String prefix, final String tableName) {
        if (StringUtils.isEmpty(prefix)) {
            return "/" + tableName.replaceAll("_", "/");
        }
        return "/" + tableName.replaceFirst(prefix, "").replaceAll("_", "/");
    }

    private static String getPack(final String entityName) {
        final int index = entityName.lastIndexOf(".");
        return entityName.substring(0, index);
    }

    private static List<ColumnData> getColumns(final Map<String, Class<?>> propertyTypeMap) {
        return propertyTypeMap.keySet().stream().map(property -> {
            final ColumnData columnData = new ColumnData();

            final Class clazz = propertyTypeMap.get(property);

            final PropertyData propertyData = new PropertyData();
            propertyData.setProperty(property);
            propertyData.setPropertyType(clazz.getSimpleName());

            columnData.setPropertyData(propertyData);
            // set default
            columnData.setForm(true);
            columnData.setList(true);
            columnData.setRequired(true);
            columnData.setFormType("el-input");
            return columnData;
        }).collect(Collectors.toList());
    }
}
