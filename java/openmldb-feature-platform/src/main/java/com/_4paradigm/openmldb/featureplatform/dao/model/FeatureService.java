package com._4paradigm.openmldb.featureplatform.dao.model;

import lombok.Data;

@Data
public class FeatureService {
    private String name;
    private String featureList;
    private String sql;
    private String deployment;

    public FeatureService() {

    }

    public FeatureService(String name, String featureList) {
        this.name = name;
        this.featureList = featureList;
    }

    public FeatureService(String name, String featureList, String sql, String deployment) {
        this.name = name;
        this.featureList = featureList;
        this.sql = sql;
        this.deployment = deployment;
    }
}
