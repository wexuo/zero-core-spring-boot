package com.zero.boot.code.data;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class TableData implements Serializable {
    private String tableName;
    private String entityName;

    private String identifierName;
    private Class<?> identifierType;

    private Map<String, Class<?>> propertyTypeMap;
}
