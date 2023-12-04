package com.zero.boot.code.data;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemData extends Property implements Serializable {
    private String label;
    private String component;
    private Boolean required;
}
