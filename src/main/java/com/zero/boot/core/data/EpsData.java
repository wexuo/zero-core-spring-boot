package com.zero.boot.core.data;

import lombok.Data;

import java.util.List;

@Data
public class EpsData {
    private String module;
    private String name;
    private List<EpsColumn> columns;
    private String prefix;
    private List<EpsApi> api;

    @Data
    public static class EpsColumn {
        private String propertyName;
        private String type;
        private String comment;
        private boolean nullable;
    }

    @Data
    public static class EpsApi {
        private String method;
        private String path;
        private String summary;
        private String prefix;
    }
}
