package com._4paradigm.openmldb.featureplatform.dao.model;

import lombok.Data;

@Data
public class FeatureView {
    private String name;
    private String entityNames;
    private String sql;
    private String featureNames;

    public FeatureView() {

    }

    public FeatureView(String name, String entityNames, String sql) {
        this.name = name;
        this.entityNames = entityNames;
        this.sql = sql;
    }

    public FeatureView(String name, String entityNames, String sql, String featureNames) {
        this.name = name;
        this.entityNames = entityNames;
        this.sql = sql;
        this.featureNames = featureNames;
    }
}
