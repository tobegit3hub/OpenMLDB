package com._4paradigm.openmldb.featureplatform.client.examples;

import com._4paradigm.openmldb.featureplatform.client.FeaturePlatformClient;
import com._4paradigm.openmldb.featureplatform.dao.model.Entity;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureService;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureView;
import com._4paradigm.openmldb.featureplatform.dao.model.SimpleTableInfo;

import java.io.IOException;
import java.util.List;

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

    public static void useFeatureViews() throws IOException {
        // List all feature views
        List<FeatureView> featureViews = client.getFeatureViews();
        System.out.println(featureViews);

        // Create a feature view
        client.createFeatureView("feature_view1", "entity1", "select col1, col2 from entity1");

        // Get a feature view
        FeatureView featureView = client.getFeatureView("feature_view1");
        System.out.println(featureView);

        // Delete a feature view
        client.deleteFeatureView("feature_view1");
    }

    public static void useFeatureServices() throws IOException {
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
    }

    public static void validateSql() throws IOException {
        String sql = "SELECT 100";
        boolean result = client.validateSql(sql);
        System.out.println(result);
    }

    public static void querySql() throws IOException {
        String sql = "SELECT 100";
        boolean result = client.querySql(sql);
        System.out.println(result);
    }

    public static void executeSql() throws IOException {
        String sql = "CREATE DATABASE db1";
        boolean result = client.executeSql(sql);
        System.out.println(result);
    }

    public static void useOtherApis() throws IOException {
        List<SimpleTableInfo> tables = client.getTables();
        System.out.println(tables);
    }

    public static void main(String[] args) {

        try {
            //useEntity();
            //validateSql();
            //executeSql();
            useOtherApis();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
