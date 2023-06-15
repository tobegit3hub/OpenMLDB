package com._4paradigm.openmldb.featureplatform.client.examples;

import com._4paradigm.openmldb.featureplatform.client.FeaturePlatformClient;
import org.apache.http.HttpResponse;

import java.io.IOException;

public class SingleTableSingleFeatureViewExample {

    public static void main(String[] args) {
        FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

        try {
            // Create test db and tables
            client.executeSql("CREATE DATABASE IF NOT EXISTS test_db");
            client.executeSql("CREATE TABLE IF NOT EXISTS test_db.user (name string, age int)");

            // Create feature view
            client.createFeatureView("featureview1", "", "SYSTEM_FEATURE_PLATFORM", "SELECT name, age + 10 AS new_age FROM user");

            // Create feature service
            client.createFeatureService("featureservice1", "featureview1");

            // Test feature service
            HttpResponse response = client.requestFeatureService("featureservice1", "{\"input\": [[\"abc\", 22]]}");
            client.printResponse(response);

            // Cleanup resources
            client.deleteFeatureService("featureservice1");
            client.deleteFeatureView("featureview1");
            client.executeSql("DROP TABLE test_db.user");
            client.executeSql("DROP DATABASE test_db");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
