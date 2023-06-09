package com._4paradigm.openmldb.featureplatform.dao;

import com._4paradigm.openmldb.jdbc.SQLResultSet;
import com._4paradigm.openmldb.sdk.impl.SqlClusterExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class OpenmldbDbService {

    private final Statement openmldbStatement;

    private final SqlClusterExecutor openmldbSqlExecutor;

    @Autowired
    public OpenmldbDbService(Statement openmldbStatement, SqlClusterExecutor openmldbSqlExecutor) {
        this.openmldbStatement = openmldbStatement;
        this.openmldbSqlExecutor = openmldbSqlExecutor;
    }

    public void initDbAndTables() throws SQLException {
        String sql = "CREATE DATABASE IF NOT EXISTS SYSTEM_FEATURE_PLATFORM";
        openmldbStatement.execute(sql);

        sql = "USE SYSTEM_FEATURE_PLATFORM";
        openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS entities (name string, primary_keys string)";
        openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS features (feature_view_name String, feature_name string, type string)";
        openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS feature_views (name string, entity_names string, sql string, feature_names string)";
        openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS feature_services (name string, feature_view_names string, sql string, deployment string)";
        openmldbStatement.execute(sql);
    }

    public boolean validateSql(String sql) {


        //SqlClusterExecutor.validateSQLInRequest();
        return true;

    }

    public SQLResultSet executeSql(String sql) throws SQLException {
        openmldbStatement.execute(sql);
        if (sql.toLowerCase().startsWith("select")) {
            return (SQLResultSet) openmldbStatement.getResultSet();
        } else {
            return null;
        }
    }

}