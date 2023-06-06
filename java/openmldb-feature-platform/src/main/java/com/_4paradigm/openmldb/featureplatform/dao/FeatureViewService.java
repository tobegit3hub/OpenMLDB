package com._4paradigm.openmldb.featureplatform.dao;

import com._4paradigm.openmldb.featureplatform.dao.model.FeatureView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FeatureViewService {

    private final Connection openmldbConnection;

    @Autowired
    public FeatureViewService(Connection openmldbConnection) {
        this.openmldbConnection = openmldbConnection;
    }

    public List<FeatureView> getFeatureViews() {
        String sql = "SELECT name, entity_names, sql FROM SYSTEM_FEATURE_PLATFORM.feature_views";

        ArrayList<FeatureView> featureViews = new ArrayList<>();

        try {
            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            ResultSet result = openmldbStatement.getResultSet();

            while (result.next()) {
                FeatureView featureView = new FeatureView(result.getString(1), result.getString(2), result.getString(3));
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

            String sql = String.format("SELECT name, entity_names, sql FROM SYSTEM_FEATURE_PLATFORM.feature_views WHERE name='%s'", name);
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
                    FeatureView featureView = new FeatureView(result.getString(1), result.getString(2), result.getString(3));
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


            // TODO: It would be better to use JDBC prepared statement from connection
            String sql = String.format("INSERT INTO SYSTEM_FEATURE_PLATFORM.feature_views (name, entity_names, sql) values ('%s', '%s', '%s')", featureView.getName(), featureView.getEntityNames(), featureView.getSql());

            Statement openmldbStatement = openmldbConnection.createStatement();
            openmldbStatement.execute(sql);
            openmldbStatement.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteFeatureView(String name) {
        try {
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