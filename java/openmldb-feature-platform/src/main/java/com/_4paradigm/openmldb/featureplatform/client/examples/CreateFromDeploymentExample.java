package com._4paradigm.openmldb.featureplatform.client.examples;

import com._4paradigm.openmldb.featureplatform.client.FeaturePlatformClient;
import org.apache.http.HttpResponse;

import java.io.IOException;

public class CreateFromDeploymentExample {

    public static void main(String[] args) {
        FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

        try {
            client.executeSql("CREATE TABLE IF NOT EXISTS SYSTEM_FEATURE_PLATFORM.t1 (c1 int, c2 int, c3 bigint)");

            // TODO: Required DEPLOY to support db name
            client.executeSql("DEPLOY t1_deploy SELECT c1 + 10 AS c1, c2 + 100 AS c2, c3 + 1000 AS c3 FROM SYSTEM_FEATURE_PLATFORM.t1");

            client.createFeatureServiceFromDeployment("t1_deploy_service", "SYSTEM_FEATURE_PLATFORM", "t1_deploy");

            // Test feature service
            String demoData  = client.getFeatureServiceRequestDemoData("t1_deploy_service");
            HttpResponse response = client.requestFeatureService("t1_deploy_service", demoData);
            client.printResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
