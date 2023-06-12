package com._4paradigm.openmldb.featureplatform.dao.model;

import lombok.Data;

@Data
public class FeatureServiceDeploymentRequest {
    private String name;
    private String db;
    private String deploymentName;

    public FeatureServiceDeploymentRequest() {

    }

    public FeatureServiceDeploymentRequest(String name, String db, String deploymentName) {
        this.name = name;
        this.db = db;
        this.deploymentName = deploymentName;
    }
}
