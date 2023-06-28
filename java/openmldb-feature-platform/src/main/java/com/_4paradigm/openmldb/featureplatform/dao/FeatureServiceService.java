package com._4paradigm.openmldb.featureplatform.dao;

import com._4paradigm.openmldb.common.Pair;
import com._4paradigm.openmldb.featureplatform.dao.model.Entity;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureService;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureServiceDeploymentRequest;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureView;
import com._4paradigm.openmldb.featureplatform.utils.OpenmldbTableUtil;
import com._4paradigm.openmldb.sdk.Column;
import com._4paradigm.openmldb.sdk.Schema;
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
import java.util.Map;
import com._4paradigm.openmldb.featureplatform.utils.TypeUtil;

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
        String sql = "SELECT name, feature_list, db, sql, deployment FROM SYSTEM_FEATURE_PLATFORM.feature_services";

        ArrayList<FeatureService> featureServices = new ArrayList<>();

        try {
            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            ResultSet result = openmldbStatement.getResultSet();

            while (result.next()) {
                FeatureService featureService = new FeatureService(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
                featureServices.add(featureService);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return featureServices;
    }

    public FeatureService getFeatureServiceByName(String name) {
        try {
            String sql = String.format("SELECT name, feature_list, db, sql, deployment FROM SYSTEM_FEATURE_PLATFORM.feature_services WHERE name='%s'", name);
            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            ResultSet result = openmldbStatement.getResultSet();

            if (result.getFetchSize() == 0) {
                System.out.println("Can not get FeatureService with the name: " + name);
                return null;
            } else if (result.getFetchSize() > 1) {
                System.out.println("Get more FeatureService with the same name");
                return null;
            } else {
                while (result.next()) {
                    FeatureService featureService = new FeatureService(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
                    return featureService;
                }
            }

            openmldbStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String mergeSqlList(SqlClusterExecutor openmldbSqlExecutor, List<String> sqlList, String db, List<String> joinKeys, Map<String, Map<String, Schema >> tableSchema) throws SQLException {
        if (sqlList.size() == 1) {
            return sqlList.get(0);
        } else {
            String mergeSql = SqlClusterExecutor.mergeSQL(sqlList, db, joinKeys, tableSchema);
            // TODO: Use for package with openmldb-0.8
            //String mergeSql = sqlList.get(0);
            System.out.println("Try to merge SQLs: " + sqlList + ", get merged SQL: " + mergeSql);
            return mergeSql;
        }
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
            FeatureView latestFeatureView = null;
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
                    // TODO: Maks sure all the features use the same db
                    latestFeatureView = featureView;
                }
            }

            String db = latestFeatureView.getDb();
            newFeatureService.setDb(db);

            if (sqlList.size()==0) {
                System.out.println("Can not get sql from feature views: " + String.join(",", featureList));
                return null;
            }

            EntityService entityService = new EntityService(openmldbConnection);
            List<String> joinKeys = new ArrayList<>();
            for (String rawEntityName: latestFeatureView.getEntityNames().split(",")) {
                if (rawEntityName != null && !rawEntityName.trim().equals("")) {
                    String entityName = rawEntityName.trim();
                    Entity entity = entityService.getEntityByName(entityName);
                    for (String rawPrimaryKey: entity.getPrimaryKeys().split(",")) {
                        joinKeys.add(rawPrimaryKey.trim());
                    }
                }
            }

            String mergedSql = mergeSqlList(openmldbSqlExecutor, sqlList, db, joinKeys, OpenmldbTableUtil.getSystemSchemaMaps(openmldbSqlExecutor));
            String deploymentName = "FEATURE_PLATFORM_" + featureService.getName();

            // If deployment is provided
            if (featureService.getDeployment() != null && !featureService.getDeployment().isEmpty()) {
                deploymentName = featureService.getDeployment();
            } else {
                if (!db.equals("")) {
                    openmldbStatement.execute("USE " + db);
                }

                // Deploy with SQL
                // TODO: Skip index check for compatibility of old OpenMLDB cluster
                String deploymentSql = String.format("DEPLOY %s OPTIONS (SKIP_INDEX_CHECK=\"TRUE\") %s", deploymentName, mergedSql);
                System.out.println("Try to create deployment with SQL: " + deploymentSql);
                openmldbStatement.execute(deploymentSql);
            }

            newFeatureService.setSql(mergedSql);
            newFeatureService.setDeployment(deploymentName);

            // TODO: It would be better to use JDBC prepared statement from connection
            String sql = String.format("INSERT INTO SYSTEM_FEATURE_PLATFORM.feature_services (name, feature_list, db, sql, deployment) values ('%s', '%s', '%s', '%s', '%s')", featureService.getName(), featureService.getFeatureList(), db, mergedSql, deploymentName);
            openmldbStatement.execute(sql);

            openmldbStatement.close();
            return newFeatureService;
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
            if (!db.equals("")) {
                openmldbStatement.execute("USE " + db);
            }

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
                featureViewService.addFeatureView(new FeatureView(featureViewName, "", db, selectSql));

                // Create feature service
                String featureList = featureViewName;
                FeatureService featureService = new FeatureService(featureServiceName, featureList, db, selectSql, deploymentName);
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
            if (!featureService.getDb().equals("")) {
                openmldbStatement.execute("USE " + featureService.getDb());
            }
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
        FeatureService featureService = featureServiceService.getFeatureServiceByName(name);
        String db = featureService.getDb();
        String deployment = featureService.getDeployment();

        HttpClient httpClient = HttpClients.createDefault();
        String endpoint = String.format("http://%s/dbs/%s/deployments/%s", apiServerEndpoint, db, deployment);
        HttpPost postRequest = new HttpPost(endpoint);
        postRequest.setHeader("Content-Type", "application/json");
        postRequest.setEntity(new StringEntity(requestData));
        HttpResponse response = httpClient.execute(postRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity responseEntity = response.getEntity();
        String responseBody = EntityUtils.toString(responseEntity);
        return new ResponseEntity<String>(responseBody, HttpStatus.valueOf(statusCode));
    }

    public Schema getRequestSchema(String serviceName) throws SQLException {
        FeatureService featureService = getFeatureServiceByName(serviceName);
        String sql = featureService.getSql();
        String db = featureService.getDb();

        List<Pair<String, String>> tables = SqlClusterExecutor.getDependentTables(sql, db, OpenmldbTableUtil.getSystemSchemaMaps(openmldbSqlExecutor));

        Pair<String, String> mainTablePair = tables.get(0);

        String mainDb = mainTablePair.getKey();
        String mainTable = mainTablePair.getValue();

        Schema schema = openmldbSqlExecutor.getTableSchema(mainDb, mainTable);
        return schema;
    }

    public String getRequestDemo(String serviceName) throws SQLException {
        Schema schema = getRequestSchema(serviceName);

        // "{\"input\": [[\"abc\", 22]]}"
        StringBuilder demoBuilder = new StringBuilder();
        demoBuilder.append("{input: [[");

        for (int i=0; i<schema.getColumnList().size(); ++i) {
            Column column = schema.getColumnList().get(i);
            column.getSqlType();
            String demoValue = TypeUtil.javaSqlTypeToDemoData(column.getSqlType());

            if (i != 0) {
                demoBuilder.append(", ");
            }

            demoBuilder.append(demoValue);
        }

        demoBuilder.append("]]}");
        return demoBuilder.toString();
    }

    public List<String> getDependentTables(String name) throws SQLException {
        FeatureService featureService = getFeatureServiceByName(name);
        List<Pair<String, String>> tables = SqlClusterExecutor.getDependentTables(featureService.getSql(), featureService.getDb(), OpenmldbTableUtil.getSystemSchemaMaps(openmldbSqlExecutor));

        List<String> fullNameTables = new ArrayList<>();
        for (Pair<String, String> tableItem: tables) {
            fullNameTables.add(tableItem.getKey() + "." + tableItem.getValue());
        }
        return fullNameTables;
    }
}