package com._4paradigm.openmldb.featureplatform.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Repository
public class OpenmldbDbService {

    private final Statement openmldbStatement;

    @Autowired
    public OpenmldbDbService(Statement openmldbStatement) {
        this.openmldbStatement = openmldbStatement;
    }

    public void initDbAndTables() throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS SYSTEM_FEATURE_PLATFORM";
        openmldbStatement.execute(sql);

        sql = "USE SYSTEM_FEATURE_PLATFORM";
        openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS entities (name string, primary_keys string)";
        openmldbStatement.execute(sql);

        //sql = "CREATE TABLE IF NOT EXISTS features (feature_view_name String, name string, type string)";
        //openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS feature_views (name string, entity_name string, sql string, feature_names string)";
        openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS feature_services (name string, feature_view_names string, sql string, deployment string)";
        openmldbStatement.execute(sql);
    }

    public boolean executeSql(String sql) {
        try {
            return openmldbStatement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ResultSet querySql(String sql) {
        try {
            openmldbStatement.execute(sql);
            ResultSet result = openmldbStatement.getResultSet();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}