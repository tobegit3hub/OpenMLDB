package com._4paradigm.openmldb.featureplatform.dao;

import com._4paradigm.openmldb.featureplatform.dao.model.FeatureService;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureServiceDeploymentRequest;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureView;
import com._4paradigm.openmldb.sdk.impl.SqlClusterExecutor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FeatureServiceService {

    @Autowired
    private Environment env;

    private final Connection openmldbConnection;
    private final SqlClusterExecutor openmldbSqlExecutor;

    @Autowired
    public FeatureServiceService(Connection openmldbConnection, SqlClusterExecutor openmldbSqlExecutor) {
        this.openmldbConnection = openmldbConnection;
        this.openmldbSqlExecutor = openmldbSqlExecutor;
    }

    public List<FeatureService> getFeatureServices() {
        String sql = "SELECT name, feature_list, sql, deployment FROM SYSTEM_FEATURE_PLATFORM.feature_services";

        ArrayList<FeatureService> featureServices = new ArrayList<>();

        try {
            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            ResultSet result = openmldbStatement.getResultSet();

            while (result.next()) {
                FeatureService featureService = new FeatureService(result.getString(1), result.getString(2), result.getString(3), result.getString(4));
                featureServices.add(featureService);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return featureServices;
    }

    public FeatureService getFeatureServiceByName(String name) {
        try {
            String sql = String.format("SELECT name, feature_list, sql, deployment FROM SYSTEM_FEATURE_PLATFORM.feature_services WHERE name='%s'", name);
            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            ResultSet result = openmldbStatement.getResultSet();

            if (result.getFetchSize() == 0) {
                System.out.print("Can not get FeatureService with the name: " + name);
                return null;
            } else if (result.getFetchSize() > 1) {
                System.out.print("Get more FeatureService with the same name");
                return null;
            } else {
                while (result.next()) {
                    FeatureService featureService = new FeatureService(result.getString(1), result.getString(2), result.getString(3), result.getString(4));
                    return featureService;
                }
            }

            openmldbStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String mergeSqlList(SqlClusterExecutor openmldbSqlExecutor, List<String> sqlList) {
        // TODO: Call mergeSQL to merge the SQLs from FeatureViews
        String mergeSql = sqlList.get(0);
        System.out.println("Try to merge SQLs: " + sqlList + ", get merged SQL: " + mergeSql);

        // TODO: Call mergeSQL when it is ready
        // SqlClusterExecutor.mergeSQL();
        return mergeSql;
    }

    public FeatureService createFeatureService(FeatureService featureService) {
        try {
            Statement openmldbStatement = openmldbConnection.createStatement();

            // Get name and feature_list
            FeatureService newFeatureService = new FeatureService();
            newFeatureService.setName(featureService.getName());
            newFeatureService.setFeatureList(featureService.getFeatureList());

            // Merge SQL from FeatureViews
            List<String> sqlList = new ArrayList<>();
            String[] featureList = featureService.getFeatureList().split(",");
            FeatureViewService featureViewService = new FeatureViewService(openmldbConnection, openmldbSqlExecutor);
            for (String splitFeatureList: featureList) {
                String featureListItem = splitFeatureList.trim();
                if (!featureListItem.equals("")) {
                    // TODO: Support get item by feature name instead of all features from feature view
                    FeatureView featureView = featureViewService.getFeatureViewByName(featureListItem);
                    if (featureView == null) {
                        System.out.println("Can not get feature view by name: " + featureListItem);
                        return null;
                    }
                    sqlList.add(featureView.getSql());
                }
            }

            if (sqlList.size()==0) {
                System.out.println("Can not get sql from feature views: " + String.join(",", featureList));
                return null;
            }

            String mergedSql = mergeSqlList(openmldbSqlExecutor, sqlList);
            String deploymentName = "FEATURE_PLATFORM_" + featureService.getName();

            // If deployment is provided
            if (!featureService.getDeployment().isEmpty()) {
                deploymentName = featureService.getDeployment();
            } else {
                // Deploy with SQL
                String deploymentSql = String.format("DEPLOY %s %s", deploymentName, mergedSql);
                System.out.println("Try to create deployment with SQL: " + deploymentSql);
                openmldbStatement.execute(deploymentSql);
            }

            // TODO: It would be better to use JDBC prepared statement from connection
            String sql = String.format("INSERT INTO SYSTEM_FEATURE_PLATFORM.feature_services (name, feature_list, sql, deployment) values ('%s', '%s', '%s', '%s')", featureService.getName(), featureService.getFeatureList(), mergedSql, deploymentName);
            openmldbStatement.execute(sql);

            openmldbStatement.close();
            return featureService;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public FeatureService createFeatureServiceFromDeployment(FeatureServiceDeploymentRequest request) {
        try {
            String featureServiceName = request.getName();
            String db = request.getDb();
            String deploymentName = request.getDeploymentName();

            Statement openmldbStatement = openmldbConnection.createStatement();
            // TODO: Support user's db
            //String sql = String.format("USE %s", db);
            //openmldbStatement.execute(sql);

            String sql = String.format("SHOW DEPLOYMENT %s", deploymentName);
            openmldbStatement.execute(sql);

            ResultSet result = openmldbStatement.getResultSet();

            while (result.next()) {
                /*
                 ......
                 --------------------------
                 SQL
                 --------------------------
                 DEPLOY demo_deploy SELECT
                   col
                 FROM
                   t1
                 ;
                 --------------------------
                 .......
                 */
                String resultsetString = result.getString(1);
                String internalSqlString = resultsetString.split("SQL")[1].trim();
                int sqlStartIndex = -1;
                int sqlEndIndex = -1;
                boolean getStartIndex = false;
                for (int i=0; i< internalSqlString.length(); ++i) {
                    if (internalSqlString.charAt(i) != '-') {
                        if (getStartIndex == false) {
                            sqlStartIndex = i;
                            getStartIndex = true;
                        }
                    }
                    if (getStartIndex && internalSqlString.charAt(i) == '-') {
                        sqlEndIndex = i - 1;
                        break;
                    }
                }

                if (sqlStartIndex <= 0 || sqlEndIndex <= 0) {
                    System.out.println("Fail to parse from SQL result: " + resultsetString);
                }

                // Remove the DEPLOY keyword
                String deploymentSql = internalSqlString.substring(sqlStartIndex, sqlEndIndex).trim();
                String selectSql = deploymentSql.substring(deploymentSql.indexOf(" ", deploymentSql.indexOf(" ") + 1)).trim().replaceAll("[\r\n]+", " ");

                // Create feature view
                // TODO: Handle the duplicated name
                String featureViewName = featureServiceName;
                FeatureViewService featureViewService = new FeatureViewService(openmldbConnection, openmldbSqlExecutor);
                featureViewService.addFeatureView(new FeatureView(featureViewName, "", selectSql));

                // Create feature service
                String featureList = featureViewName;
                FeatureService featureService = new FeatureService(featureServiceName, featureList, selectSql, deploymentName);
                return createFeatureService(featureService);
            }

        } catch (SQLException e) {
            System.out.println("Fail to create feature service from deployment, get exception: " + e.getMessage());
        }

        return null;
    }

    public boolean deleteFeatureService(String name) {
        try {
            Statement openmldbStatement = openmldbConnection.createStatement();

            // Delete the deployment
            FeatureService featureService = getFeatureServiceByName(name);
            String dropDeploymentSql = String.format("DROP DEPLOYMENT %s", featureService.getDeployment());
            System.out.println("Try to drop deployment with sql: " + dropDeploymentSql);
            openmldbStatement.execute(dropDeploymentSql);

            // TODO: It would be better to use JDBC prepared statement from connection
            String sql = String.format("DELETE FROM SYSTEM_FEATURE_PLATFORM.feature_services WHERE name='%s'", name);
            openmldbStatement.execute(sql);

            openmldbStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ResponseEntity<String> requestFeatureService(String name, String requestData) throws IOException {
        String apiServerEndpoint = env.getProperty("openmldb.apiserver");
        if (apiServerEndpoint == null || apiServerEndpoint.equals("")) {
            throw new IOException("Need to config openmldb.apiserver in application.yaml");
        }

        // TODO: Get the db from feature service
        FeatureServiceService featureServiceService = new FeatureServiceService(openmldbConnection, openmldbSqlExecutor);
        String deployment = featureServiceService.getFeatureServiceByName(name).getDeployment();

        HttpClient httpClient = HttpClients.createDefault();
        String endpoint = String.format("http://%s/dbs/SYSTEM_FEATURE_PLATFORM/deployments/%s", apiServerEndpoint, deployment);
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(requestData));
        HttpResponse response = httpClient.execute(postRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity responseEntity = response.getEntity();
        String responseBody = EntityUtils.toString(responseEntity);
        return new ResponseEntity<String>(responseBody, HttpStatus.valueOf(statusCode));
    }
}