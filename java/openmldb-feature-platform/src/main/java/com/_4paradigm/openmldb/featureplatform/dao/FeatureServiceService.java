package com._4paradigm.openmldb.featureplatform.dao;

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
            String sql = "SELECT name, feature_view_names FROM SYSTEM_FEATURE_PLATFORM.feature_services WHERE name=?";

            PreparedStatement openmldbStatement = openmldbConnection.prepareStatement(sql);
            openmldbStatement.setString(1, name);

            ResultSet result = openmldbStatement.executeQuery();

            if (result.getFetchSize() == 0) {
                System.out.print("Can not get FeatureService with the name: " + name);
                return null;
            } else if (result.getFetchSize() > 1) {
                System.out.print("Get more FeatureService with the same name");
                return null;
            } else {
                while (result.next()) {
                    FeatureService featureService = new FeatureService(result.getString(1), result.getString(2));
                    return featureService;
                }
            }

            openmldbStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean addFeatureService(FeatureService featureService) {
        try {
            // TODO: It would be better to use JDBC prepared statement from connection
            String sql = String.format("INSERT INTO SYSTEM_FEATURE_PLATFORM.feature_services values ('%s', '%s', '', '')", featureService.getName(), featureService.getFeatureViewsNames());

            Statement openmldbStatement = openmldbConnection.createStatement();
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