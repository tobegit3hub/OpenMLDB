package com._4paradigm.openmldb.featureplatform.dao.model;

import lombok.Data;

@Data
public class FeatureView {
    private String name;
    private String entityNames;
    private String db;
    private String sql;
    private String featureNames;
    private String description;

    public FeatureView() {

    }

    public FeatureView(String name, String entityNames, String db, String sql) {
        this.name = name;
        this.entityNames = entityNames;
        this.db = db;
        this.sql = sql;
    }

    public FeatureView(String name, String entityNames, String db, String sql, String featureNames) {
        this.name = name;
        this.entityNames = entityNames;
        this.db = db;
        this.sql = sql;
        this.featureNames = featureNames;
        this.description = "";
    }

    public FeatureView(String name, String entityNames, String db, String sql, String featureNames, String description) {
        this.name = name;
        this.entityNames = entityNames;
        this.db = db;
        this.sql = sql;
        this.featureNames = featureNames;
        this.description = description;
    }
}
