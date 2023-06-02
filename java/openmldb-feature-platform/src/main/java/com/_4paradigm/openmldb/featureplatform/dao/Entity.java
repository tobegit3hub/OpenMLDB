package com._4paradigm.openmldb.featureplatform.dao;

import lombok.Data;

@Data
public class Entity {
    private String name;
    private String primary_keys;

    public Entity(String name, String primary_keys) {
        this.name = name;
        this.primary_keys = primary_keys;
    }
}
