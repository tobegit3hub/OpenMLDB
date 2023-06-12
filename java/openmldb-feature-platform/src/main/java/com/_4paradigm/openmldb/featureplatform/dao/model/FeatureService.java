package com._4paradigm.openmldb.featureplatform.dao.model;

import lombok.Data;

@Data
public class FeatureService {
    private String name;
    private String featureList;
    private String db;
    private String sql;
    private String deployment;

    public FeatureService() {

    }

    public FeatureService(String name, String featureList, String db) {
        this.name = name;
        this.featureList = featureList;
        this.db = db;
    }

    public FeatureService(String name, String featureList, String db, String sql, String deployment) {
        this.name = name;
        this.featureList = featureList;
        this.db = db;
        this.sql = sql;
        this.deployment = deployment;
    }
}
