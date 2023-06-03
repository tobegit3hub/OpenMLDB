package com._4paradigm.openmldb.featureplatform.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com._4paradigm.openmldb.featureplatform.dao.Entity;

public class FeaturePlatformClient {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private HttpClient httpClient;
    private String apiEndpoint;

    public FeaturePlatformClient(String host, int port) {
        this.httpClient = HttpClients.createDefault();
        this.apiEndpoint = String.format("http://%s:%d/api/", host, port);
    }

    private static void printResponse(HttpResponse response) throws IOException {
        int responseCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Body: " + responseBody);
        EntityUtils.consume(entity);
    }

    public List<Entity> listEntities() throws IOException {
        String endpoint = this.apiEndpoint + "entities";
        HttpGet request = new HttpGet(endpoint);
        HttpResponse response = httpClient.execute(request);
        //printResponse(response);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);

        //return objectMapper.readValue(responseBody, CustomObject.class);
        return objectMapper.readValue(responseBody, new TypeReference<List<Entity>>() {});
    }

    public boolean createEntity(String name, String primaryKeys) throws IOException {
        String endpoint = this.apiEndpoint + "entities";
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
        List<NameValuePair> postParameters = new ArrayList<>();
        postRequest.setEntity(new StringEntity(String.format("{\"name\":\"%s\", \"primaryKeys\":\"%s\"}", name, primaryKeys)));
        HttpResponse postResponse = httpClient.execute(postRequest);
        printResponse(postResponse);
        // TODO: Check response status code
        return true;
    }

    public Entity getEntity(String name) throws IOException {
        String endpoint = this.apiEndpoint + "entities/" + name;
        HttpGet request = new HttpGet(endpoint);
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        return objectMapper.readValue(responseBody, Entity.class);
    }

}
