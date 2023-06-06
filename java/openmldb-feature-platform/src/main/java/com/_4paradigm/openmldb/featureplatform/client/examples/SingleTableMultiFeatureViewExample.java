package com._4paradigm.openmldb.featureplatform.client.examples;

import com._4paradigm.openmldb.featureplatform.client.FeaturePlatformClient;

import java.io.IOException;

public class SingleTableMultiFeatureViewExample {

    public static void main(String[] args) {
        FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

        try {
            // Create test db and tables
            client.executeSql("CREATE DATABASE IF NOT EXISTS test_db");
            client.executeSql("CREATE TABLE IF NOT EXISTS test_db.user (name string, age int)");

            // Create entity
            client.createEntity("name", "name");

            // Create feature view
            client.createFeatureView("featureview1", "name", "SELECT name FROM test_db.user");
            client.createFeatureView("featureview2", "name", "SELECT age + 10 AS new_age FROM test_db.user");

            // Create feature service
            client.createFeatureService("featureservice1", "featureview1, featureview2");

            // Test feature service
            client.testFeatureService("http://127.0.0.1:9080", "featureservice1", "{\"input\": [[\"abc\", 22]]}");

            // Cleanup resources
            client.deleteFeatureService("featureservice1");
            client.deleteFeatureView("featureview1");
            client.deleteFeatureView("featureview2");
            client.deleteEntity("name");
            client.executeSql("DROP TABLE test_db.user");
            client.executeSql("DROP DATABASE test_db");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
