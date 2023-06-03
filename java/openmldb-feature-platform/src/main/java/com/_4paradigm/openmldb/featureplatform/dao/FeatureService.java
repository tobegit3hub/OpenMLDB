package com._4paradigm.openmldb.featureplatform.dao;

import lombok.Data;

import java.util.List;

@Data
public class FeatureService {
    private String name;
    private String featureViewsNames;

    public FeatureService() {

    }

    public FeatureService(String name, String featureViewsNames) {
        this.name = name;
        this.featureViewsNames = featureViewsNames;
    }
}
