package com._4paradigm.openmldb.featureplatform.dao.model;

import lombok.Data;

@Data
public class Feature {
    private String featureViewName;
    private String featureName;
    private String type;

    public Feature() {

    }

    public Feature(String featureViewName, String featureName, String type) {
        this.featureViewName = featureViewName;
        this.featureName = featureName;
        this.type = type;
    }

}
