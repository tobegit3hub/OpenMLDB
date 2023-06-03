package com._4paradigm.openmldb.featureplatform.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.SQLException;
import java.sql.Statement;

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

        sql = "CREATE TABLE IF NOT EXISTS features (feature_view_name String, name string, type string)";
        openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS feature_views (name string, sql string, entity_name string, feature_names string)";
        openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS feature_services (name string, feature_view_names string, sql string, deployment string)";
        openmldbStatement.execute(sql);
    }

}