package com.zero.boot.code.data;

import lombok.Data;

import java.io.Serializable;

@Data
public class ColumnData implements Serializable {
    private Boolean list;
    private Boolean form;
    private Boolean required;
    private String formType;

    private String queryType;

    private PropertyData propertyData;
}
