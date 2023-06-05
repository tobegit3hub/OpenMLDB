package com._4paradigm.openmldb.featureplatform.client.examples;

import com._4paradigm.openmldb.featureplatform.client.FeaturePlatformClient;

import java.io.IOException;

public class SingleTableFeatureServiceExample {

    public static void main(String[] args) {
        FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

        try {

            // Create test db and tables
            client.executeSql("CREATE DATABASE IF NOT EXISTS test_db");
            client.executeSql("CREATE TABLE IF NOT EXISTS test_db.user (name string, age int)");

            // Create feature view
            client.createFeatureView("featureview1", "", "select name, age from test_db.user");

            // Create feature service
            client.createFeatureService("featureservice2", "featureview2");

            // Test feature service



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
