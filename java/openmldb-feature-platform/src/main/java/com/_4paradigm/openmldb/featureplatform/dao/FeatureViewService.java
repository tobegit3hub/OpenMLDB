package com._4paradigm.openmldb.featureplatform.dao;

import com._4paradigm.openmldb.featureplatform.dao.model.Feature;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureService;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureView;
import com._4paradigm.openmldb.featureplatform.utils.OpenmldbTableUtil;
import com._4paradigm.openmldb.featureplatform.utils.TypeUtil;
import com._4paradigm.openmldb.sdk.Column;
import com._4paradigm.openmldb.sdk.Schema;
import com._4paradigm.openmldb.sdk.impl.SqlClusterExecutor;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

@Repository
public class FeatureViewService {

    private final Connection openmldbConnection;
    private final SqlClusterExecutor openmldbSqlExecutor;

    @Autowired
    public FeatureViewService(Connection openmldbConnection, SqlClusterExecutor openmldbSqlExecutor) {
        this.openmldbConnection = openmldbConnection;
        this.openmldbSqlExecutor = openmldbSqlExecutor;
    }

    public List<FeatureView> getFeatureViews() {
        String sql = "SELECT name, entity_names, db, sql, feature_names FROM SYSTEM_FEATURE_PLATFORM.feature_views";

        ArrayList<FeatureView> featureViews = new ArrayList<>();

        try {
            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            ResultSet result = openmldbStatement.getResultSet();

            while (result.next()) {
                FeatureView featureView = new FeatureView(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5));
                featureViews.add(featureView);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return featureViews;
    }

    public FeatureView getFeatureViewByName(String name) {
        try {
            // TODO: Set database before
            /*
            String sql = "SELECT name, entity_names, sql FROM SYSTEM_FEATURE_PLATFORM.feature_views WHERE name=?";
            PreparedStatement openmldbStatement = openmldbConnection.prepareStatement(sql);
            openmldbStatement.setString(1, name);
            ResultSet result = openmldbStatement.executeQuery();
            */

            String sql = String.format("SELECT name, entity_names, db, sql FROM SYSTEM_FEATURE_PLATFORM.feature_views WHERE name='%s'", name);
            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            ResultSet result = openmldbStatement.getResultSet();

            if (result.getFetchSize() == 0) {
                System.out.print("Can not get FeatureView with the name: " + name);
                return null;
            } else if (result.getFetchSize() > 1) {
                System.out.print("Get more FeatureView with the same name");
                return null;
            } else {
                while (result.next()) {
                    FeatureView featureView = new FeatureView(result.getString(1), result.getString(2), result.getString(3), result.getString(4));
                    System.out.print("Get feature view: " + featureView);
                    return featureView;
                }
            }

            openmldbStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("Fail to get feature view with name: " + name);
        return null;
    }

    public boolean addFeatureView(FeatureView featureView) {
        try {
            // TODO: Get feature names by compiling SQL

            Map<String, Map<String, Schema>> schemaMaps = OpenmldbTableUtil.getSystemSchemaMaps(openmldbSqlExecutor);

            String sql = featureView.getSql();

            // TODO: Validate SQL before creating

            // TODO(huangwei)：Need to support SQL which contains database name
            StringBuilder featureNamesBuilder = new StringBuilder();

            try {
                List<Column> outputSchemaColumns = SqlClusterExecutor.genOutputSchema(sql, schemaMaps).getColumnList();
                for (Column outputSchemaColumn: outputSchemaColumns) {
                    String name = outputSchemaColumn.getColumnName();
                    int intType = outputSchemaColumn.getSqlType();
                    String stringType = TypeUtil.javaSqlTypeToString(intType);

                    FeaturesService featuresService = new FeaturesService(openmldbConnection, openmldbSqlExecutor);
                    Feature feature = new Feature(featureView.getName(), name, stringType);
                    featuresService.addFeature(feature);

                    if (featureNamesBuilder.length() == 0) {
                        featureNamesBuilder.append(name);
                    } else {
                        featureNamesBuilder.append(", " + name);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Notice that we can not pass the SQL with database now, skip creating Feature objects");
            }

            // TODO: It would be better to use JDBC prepared statement from connection
            String featureNames = featureNamesBuilder.toString();
            String insertSql = String.format("INSERT INTO SYSTEM_FEATURE_PLATFORM.feature_views (name, entity_names, db, sql, feature_names) values ('%s', '%s', '%s', '%s', '%s')", featureView.getName(), featureView.getEntityNames(), featureView.getDb(), featureView.getSql(), featureNames);

            System.out.println("Try to insert with SQL: " + insertSql);
            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(insertSql);
            openmldbStatement.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteFeatureView(String name) {
        try {
            // Delete the features
            FeaturesService featuresService = new FeaturesService(openmldbConnection, openmldbSqlExecutor);
            List<Feature> features = featuresService.getFeaturesByFeatureView(name);
            for (Feature feature: features) {
                 featuresService.deleteFeature(feature);
            }

            // Delete the feature view
            // TODO: It would be better to use JDBC prepared statement from connection
            String sql = String.format("DELETE FROM SYSTEM_FEATURE_PLATFORM.feature_views WHERE name='%s'", name);

            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            openmldbStatement.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}