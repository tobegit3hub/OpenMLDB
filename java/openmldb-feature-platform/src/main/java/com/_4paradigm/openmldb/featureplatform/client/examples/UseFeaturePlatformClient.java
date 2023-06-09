package com._4paradigm.openmldb.featureplatform.client.examples;

import java.io.IOException;
import java.util.List;
import com._4paradigm.openmldb.featureplatform.client.FeaturePlatformClient;
import com._4paradigm.openmldb.featureplatform.dao.model.*;
import org.apache.http.HttpResponse;

public class UseFeaturePlatformClient {

    private static FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

    public static void useEntity() throws IOException {
        // List all entities
        List<Entity> entities = client.getEntities();
        System.out.println(entities);

        // Create an entity
        client.createEntity("entity1", "pk1, pk2");

        // Get an entity
        Entity entity = client.getEntity("entity1");
        System.out.println(entity);

        // Delete an entity
        client.deleteEntity("entity1");
    }

    public static void useFeatures() throws IOException {
        // List all features
        List<Feature> features = client.getFeatures();
        System.out.println(features);

        // List all features of feature view
        List<Feature> features2 = client.getFeaturesFromFeatureView("featureview1");
        System.out.println(features2);

        // Get a feature
        Feature feature = client.getFeature("featureview1", "feature1");
        System.out.println(feature);
    }

    public static void useFeatureViews() throws IOException {
        // List all feature views
        List<FeatureView> featureViews = client.getFeatureViews();
        System.out.println(featureViews);

        // Create a feature view
        client.createFeatureView("featureview1", "", "SELECT name, age + 10 AS new_age FROM user");

        // Get a feature view
        FeatureView featureView = client.getFeatureView("featureview1");
        System.out.println(featureView);

        // Delete a feature view
        client.deleteFeatureView("featureview1");
    }

    public static void useFeatureServices() throws IOException {
        /*
        // List all feature services
        List<FeatureService> featureServices = client.getFeatureServices();
        System.out.println(featureServices);

        // Create a feature service
        client.createFeatureService("feature_service_1", "feature_view1, feature_view2");

        // Get a feature view
        FeatureService featureService = client.getFeatureService("feature_service_1");
        System.out.println(featureService);

        // Delete a feature view
        client.deleteFeatureService("feature_service_1");
*/
        HttpResponse response = client.requestFeatureService("service1", "{\"input\": [[\"abc\", 22]]}");
        client.printResponse(response);
    }


    public static void requestApiServer() throws IOException {
        HttpResponse response = client.requestApiServer("http://127.0.0.1:9080", "service1", "{\"input\": [[\"abc\", 22]]}");
        client.printResponse(response);
    }


    public static void validateSql() throws IOException {
        String sql = "SELECT 100";
        boolean result = client.validateSql(sql);
        System.out.println(result);
    }

    public static void accessExecuteSql() throws IOException {

        String sql = "CREATE DATABASE db1";
        HttpResponse response = client.executeSql(sql);
        client.printResponse(response);

        String sql2 = "SELECT 'abc', 100";
        HttpResponse response2 = client.executeSql(sql2);
        client.printResponse(response2);
    }

    public static void useTables() throws IOException {
        List<SimpleTableInfo> tables = client.getTables();
        System.out.println(tables);

        SimpleTableInfo table = client.getTable("db1", "t1");
        System.out.println(table);
    }

    public static void main(String[] args) {
        try {
            //useEntity();
            //useFeatures();
            //useFeatureViews();
            //validateSql();
            //executeSql();
            //useOtherApis();
            //useFeatureServices();
            //requestApiServer();
            accessExecuteSql();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
