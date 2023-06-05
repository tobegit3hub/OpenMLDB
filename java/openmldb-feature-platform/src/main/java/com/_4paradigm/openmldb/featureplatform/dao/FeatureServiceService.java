package com._4paradigm.openmldb.featureplatform.dao;

import com._4paradigm.openmldb.featureplatform.dao.model.FeatureService;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FeatureServiceService {

    private final Connection openmldbConnection;

    @Autowired
    public FeatureServiceService(Connection openmldbConnection) {
        this.openmldbConnection = openmldbConnection;
    }

    public List<FeatureService> getFeatureServices() {
        String sql = "SELECT name, feature_view_names FROM SYSTEM_FEATURE_PLATFORM.feature_services";

        ArrayList<FeatureService> featureServices = new ArrayList<>();

        try {
            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            ResultSet result = openmldbStatement.getResultSet();

            while (result.next()) {
                FeatureService featureService = new FeatureService(result.getString(1), result.getString(2));
                featureServices.add(featureService);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return featureServices;
    }

    public FeatureService getFeatureServiceByName(String name) {
        try {
            // TODO: Set database before
            /*
            String sql = "SELECT name, feature_view_names FROM SYSTEM_FEATURE_PLATFORM.feature_services WHERE name=?";
            PreparedStatement openmldbStatement = openmldbConnection.prepareStatement(sql);
            openmldbStatement.setString(1, name);
            ResultSet result = openmldbStatement.executeQuery();
            */

            String sql = String.format("SELECT name, feature_view_names, sql, deployment FROM SYSTEM_FEATURE_PLATFORM.feature_services WHERE name='%s'", name);
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

    public String mergeSqlList(List<String> sqlList) {
        // TODO: Call mergeSQL to merge the SQLs from FeatureViews
        String mergeSql = sqlList.get(0);
        System.out.println("Try to merge SQLs: " + sqlList + ", get merged SQL: " + mergeSql);
        return mergeSql;
    }

    public boolean addFeatureService(FeatureService featureService) {
        try {
            Statement openmldbStatement = openmldbConnection.createStatement();

            // Get name and feature_view_names
            FeatureService newFeatureService = new FeatureService();
            newFeatureService.setName(featureService.getName());
            newFeatureService.setFeatureViewNames(featureService.getFeatureViewNames());

            // Merge SQL from FeatureViews
            List<String> sqlList = new ArrayList<>();
            String[] featureViewNames = featureService.getFeatureViewNames().split(",");
            FeatureViewService featureViewService = new FeatureViewService(openmldbConnection);
            for (String featureViewName: featureViewNames) {
                FeatureView featureView = featureViewService.getFeatureViewByName(featureViewName);
                sqlList.add(featureView.getSql());
            }

            String mergedSql = mergeSqlList(sqlList);

            String deploymentName = "FEATURE_PLATFORM_" + featureService.getName();
            String deploymentSql = String.format("DEPLOY %s %s", deploymentName, mergedSql);
            System.out.println("Try to create deployment with SQL: " + deploymentSql);
            openmldbStatement.execute(deploymentSql);

            // TODO: It would be better to use JDBC prepared statement from connection
            String sql = String.format("INSERT INTO SYSTEM_FEATURE_PLATFORM.feature_services (name, feature_view_names, sql, deployment) values ('%s', '%s', '%s', '%s')", featureService.getName(), featureService.getFeatureViewNames(), mergedSql, deploymentName);
            openmldbStatement.execute(sql);

            openmldbStatement.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteFeatureService(String name) {
        try {
            // TODO: It would be better to use JDBC prepared statement from connection
            String sql = String.format("DELETE FROM SYSTEM_FEATURE_PLATFORM.feature_services WHERE name='%s'", name);

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