package com.zero.boot.code.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class QueryData extends PropertyData implements Serializable {
    private String queryType;
}
