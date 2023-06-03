package com._4paradigm.openmldb.featureplatform.dao;

import lombok.Data;

@Data
public class FeatureView {
    private String name;
    private String entityName;
    private String sql;

    public FeatureView() {

    }

    public FeatureView(String name, String entityName, String sql) {
        this.name = name;
        this.entityName = entityName;
        this.sql = sql;
    }
}
