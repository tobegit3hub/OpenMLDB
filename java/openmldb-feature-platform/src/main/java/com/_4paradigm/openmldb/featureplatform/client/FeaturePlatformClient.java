package com._4paradigm.openmldb.featureplatform.client;

import com._4paradigm.openmldb.featureplatform.dao.model.FeatureService;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.List;
import com._4paradigm.openmldb.featureplatform.dao.model.Entity;

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

    /**
     * Get all OpenMLDB tables.
     * TODO: Notice that now it can get system tables and no table schema.
     *
     * @return
     * @throws IOException
     */
    public List<String> getTables() throws IOException {
        String endpoint = this.apiEndpoint + "tables";
        HttpGet request = new HttpGet(endpoint);
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        return objectMapper.readValue(responseBody, new TypeReference<List<String>>() {});
    }

    public List<Entity> getEntities() throws IOException {
        String endpoint = this.apiEndpoint + "entities";
        HttpGet request = new HttpGet(endpoint);
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        return objectMapper.readValue(responseBody, new TypeReference<List<Entity>>() {});
    }

    public boolean createEntity(String name, String primaryKeys) throws IOException {
        String endpoint = this.apiEndpoint + "entities";
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
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

    public boolean deleteEntity(String name) throws IOException {
        String endpoint = this.apiEndpoint + "entities/" + name;
        HttpDelete deleteRequest = new HttpDelete(endpoint);
        HttpResponse deleteResponse = httpClient.execute(deleteRequest);
        printResponse(deleteResponse);
        return true;
    }

    public List<FeatureView> getFeatureViews() throws IOException {
        String endpoint = this.apiEndpoint + "featureviews";
        HttpGet request = new HttpGet(endpoint);
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        return objectMapper.readValue(responseBody, new TypeReference<List<FeatureView>>() {});
    }

    public boolean createFeatureView(String name, String entityName, String sql) throws IOException {
        String endpoint = this.apiEndpoint + "featureviews";
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(String.format("{\"name\":\"%s\", \"entityName\":\"%s\", \"sql\":\"%s\"}", name, entityName, sql)));
        HttpResponse postResponse = httpClient.execute(postRequest);
        printResponse(postResponse);
        // TODO: Check response status code
        return true;
    }

    public FeatureView getFeatureView(String name) throws IOException {
        String endpoint = this.apiEndpoint + "featureviews/" + name;
        HttpGet request = new HttpGet(endpoint);
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        return objectMapper.readValue(responseBody, FeatureView.class);
    }

    public boolean deleteFeatureView(String name) throws IOException {
        String endpoint = this.apiEndpoint + "featureviews/" + name;
        HttpDelete deleteRequest = new HttpDelete(endpoint);
        HttpResponse deleteResponse = httpClient.execute(deleteRequest);
        printResponse(deleteResponse);
        return true;
    }

    public List<FeatureService> getFeatureServices() throws IOException {
        String endpoint = this.apiEndpoint + "featureservices";
        HttpGet request = new HttpGet(endpoint);
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        return objectMapper.readValue(responseBody, new TypeReference<List<FeatureService>>() {});
    }

    public boolean createFeatureService(String name, String feature_view_names) throws IOException {
        String endpoint = this.apiEndpoint + "featureservices";
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(String.format("{\"name\":\"%s\", \"featureViewNames\":\"%s\"}", name, feature_view_names)));
        HttpResponse postResponse = httpClient.execute(postRequest);
        printResponse(postResponse);
        // TODO: Check response status code
        return true;
    }

    public FeatureService getFeatureService(String name) throws IOException {
        String endpoint = this.apiEndpoint + "featureservices/" + name;
        HttpGet request = new HttpGet(endpoint);
        HttpResponse response = httpClient.execute(request);

        HttpEntity entity = response.getEntity();
        String responseBody = EntityUtils.toString(entity);
        return objectMapper.readValue(responseBody, FeatureService.class);
    }

    public boolean deleteFeatureService(String name) throws IOException {
        String endpoint = this.apiEndpoint + "featureservices/" + name;
        HttpDelete deleteRequest = new HttpDelete(endpoint);
        HttpResponse deleteResponse = httpClient.execute(deleteRequest);
        printResponse(deleteResponse);
        return true;
    }

    public boolean validateSql(String sql) throws IOException {
        String endpoint = this.apiEndpoint + "sql/validate";
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(String.format("{\"sql\":\"%s\"}", sql)));
        HttpResponse postResponse = httpClient.execute(postRequest);
        printResponse(postResponse);
        // TODO: Check response status code
        return true;
    }

    public boolean querySql(String sql) throws IOException {
        String endpoint = this.apiEndpoint + "sql/query";
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(String.format("{\"sql\":\"%s\"}", sql)));
        HttpResponse postResponse = httpClient.execute(postRequest);
        printResponse(postResponse);
        // TODO: Check response status code
        return true;
    }

    public boolean executeSql(String sql) throws IOException {
        String endpoint = this.apiEndpoint + "sql/execute";
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(String.format("{\"sql\":\"%s\"}", sql)));
        HttpResponse postResponse = httpClient.execute(postRequest);
        printResponse(postResponse);
        // TODO: Check response status code
        return true;
    }

    public HttpResponse requestFeatureService(String apiServerEndpoint, String featureService, String requestData) throws IOException {
        String endpoint = String.format("%s/dbs/SYSTEM_FEATURE_PLATFORM/deployments/FEATURE_PLATFORM_%s", apiServerEndpoint, featureService);
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(requestData));
        HttpResponse postResponse = httpClient.execute(postRequest);
        return postResponse;
    }

    public void testFeatureService(String apiServerEndpoint, String featureService, String requestData) throws IOException {
        HttpResponse response = requestFeatureService(apiServerEndpoint, featureService, requestData);
        printResponse(response);
    }
}
