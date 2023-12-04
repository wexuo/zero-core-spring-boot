package com.zero.boot.code.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class PropertyData extends Property implements Serializable {
    private String propertyType;
}
