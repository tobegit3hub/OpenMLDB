package com._4paradigm.openmldb.featureplatform.client;

import com._4paradigm.openmldb.featureplatform.dao.Entity;
import com._4paradigm.openmldb.featureplatform.dao.FeatureService;
import com._4paradigm.openmldb.featureplatform.dao.FeatureView;
import java.io.IOException;
import java.util.List;

public class ClientExample {

    public static void main(String[] args) {

        FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

        try {
            // List all entities
            //List<Entity> entities = client.listEntities();
            //System.out.println(entities);

            // Create an entity
            //client.createEntity("entity1", "pk1, pk2");

            // Get an entity
            //Entity entity = client.getEntity("entity1");
            //System.out.println(entity);

            // Delete an entity
            //client.deleteEntity("entity1");

            // List all feature views
            //List<FeatureView> featureViews = client.listFeatureViews();
            //System.out.println(featureViews);

            // Create a feature view
            //client.createFeatureView("feature_view1", "entity1", "select col1, col2 from entity1");

            // Get a feature view
            //FeatureView featureView = client.getFeatureView("feature_view1");
            //System.out.println(featureView);

            // Delete a feature view
            //client.deleteFeatureView("feature_view1");

            // List all feature services
            List<FeatureService> featureServices = client.listFeatureServices();
            System.out.println(featureServices);

            // Create a feature service
            client.createFeatureService("feature_service_1", "feature_view1, feature_view2");

            // Get a feature view
            FeatureService featureService = client.getFeatureService("feature_service_1");
            System.out.println(featureService);

            // Delete a feature view
            client.deleteFeatureService("feature_service_1");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
