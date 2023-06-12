package com._4paradigm.openmldb.featureplatform.dao;

import com._4paradigm.openmldb.featureplatform.utils.OpenmldbTableUtil;
import com._4paradigm.openmldb.jdbc.SQLResultSet;
import com._4paradigm.openmldb.sdk.Schema;
import com._4paradigm.openmldb.sdk.impl.SqlClusterExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class SqlService {

    private final Statement openmldbStatement;

    private final SqlClusterExecutor openmldbSqlExecutor;

    @Autowired
    public SqlService(Statement openmldbStatement, SqlClusterExecutor openmldbSqlExecutor) {
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

        sql = "CREATE TABLE IF NOT EXISTS feature_views (name string, entity_names string, db string, sql string, feature_names string)";
        openmldbStatement.execute(sql);

        sql = "CREATE TABLE IF NOT EXISTS feature_services (name string, feature_list string, db string, sql string, deployment string)";
        openmldbStatement.execute(sql);
    }

    public SQLResultSet executeSql(String sql) throws SQLException {
        openmldbStatement.execute(sql);
        if (sql.toLowerCase().startsWith("select")) {
            return (SQLResultSet) openmldbStatement.getResultSet();
        } else {
            return null;
        }
    }

    public List<String> validateSql(String sql) throws SQLException {
        Map<String, Map<String, Schema>> schemaMaps = OpenmldbTableUtil.getSystemSchemaMaps(openmldbSqlExecutor);
        List<String> result = SqlClusterExecutor.validateSQLInRequest(sql, schemaMaps);
        return result;
    }

}