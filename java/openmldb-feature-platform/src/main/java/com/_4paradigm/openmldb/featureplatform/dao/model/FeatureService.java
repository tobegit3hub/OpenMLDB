package com._4paradigm.openmldb.featureplatform.dao.model;

import lombok.Data;

@Data
public class FeatureService {
    private String name;
    private String featureViewNames;
    private String sql;
    private String deployment;

    public FeatureService() {

    }

    public FeatureService(String name, String featureViewNames) {
        this.name = name;
        this.featureViewNames = featureViewNames;
    }

    public FeatureService(String name, String featureViewNames, String sql, String deployment) {
        this.name = name;
        this.featureViewNames = featureViewNames;
        this.sql = sql;
        this.deployment = deployment;
    }
}
