package com._4paradigm.openmldb.featureplatform.client.examples;

import com._4paradigm.openmldb.featureplatform.client.FeaturePlatformClient;

import java.io.IOException;

public class MultiTableSingleFeatureViewExample {

    public static void main(String[] args) {
        FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

        try {
            // Create test db and tables
            client.executeSql("CREATE DATABASE IF NOT EXISTS test_db");
            client.executeSql("CREATE TABLE IF NOT EXISTS test_db.user (name string, age int)");
            client.executeSql("CREATE TABLE IF NOT EXISTS test_db.trade (user_name string, amount float)");

            // Create feature view
            client.createFeatureView("featureview1", "", "SELECT name, age, amount FROM test_db.user LAST JOIN test_db.trade ON test_db.user.name = test_db.trade.user_name");

            // Create feature service
            client.createFeatureService("featureservice1", "featureview1");

            // Test feature service
            client.requestFeatureService("featureservice1", "{\"input\": [[\"abc\", 22]]}");

            // Cleanup resources
            client.deleteFeatureService("featureservice1");
            client.deleteFeatureView("featureview1");
            client.executeSql("DROP TABLE test_db.user");
            client.executeSql("DROP TABLE test_db.trade");
            client.executeSql("DROP DATABASE test_db");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
