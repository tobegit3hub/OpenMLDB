package com._4paradigm.openmldb.featureplatform.client;

import com._4paradigm.openmldb.featureplatform.dao.Entity;

import java.io.IOException;
import java.util.List;

public class ClientExample {

    public static void main(String[] args) {

        FeaturePlatformClient client = new FeaturePlatformClient("127.0.0.1", 8888);

        try {
            // List all entities
            List<Entity> entities = client.listEntities();
            System.out.println(entities);

            // Create an entity
            //client.createEntity("entity1", "pk1, pk2");

            // Get an entity
            //Entity entity = client.getEntity("entity1");
            //System.out.println(entity);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
