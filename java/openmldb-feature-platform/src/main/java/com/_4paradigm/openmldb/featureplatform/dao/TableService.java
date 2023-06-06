package com._4paradigm.openmldb.featureplatform.dao;

import com._4paradigm.openmldb.featureplatform.dao.model.SimpleTableInfo;
import com._4paradigm.openmldb.sdk.Schema;
import com._4paradigm.openmldb.sdk.impl.SqlClusterExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TableService {

    private final Statement openmldbStatement;
    private final SqlClusterExecutor openmldbSqlExecutor;

    @Autowired
    public TableService(Statement openmldbStatement, SqlClusterExecutor openmldbSqlExecutor) {
        this.openmldbStatement = openmldbStatement;
        this.openmldbSqlExecutor = openmldbSqlExecutor;
    }

    public List<SimpleTableInfo> getTables() {
        ArrayList<SimpleTableInfo> simpleTableInfos = new ArrayList<>();

        List<String> databases = openmldbSqlExecutor.showDatabases();
        for (String database: databases) {
            List<String> tables = openmldbSqlExecutor.getTableNames(database);
            for (String table: tables) {
                try {
                    Schema schema = openmldbSqlExecutor.getTableSchema(database, table);
                    String schemaString = schema.toString();
                    SimpleTableInfo simpleTableInfo = new SimpleTableInfo(database, table, schemaString);
                    simpleTableInfos.add(simpleTableInfo);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return simpleTableInfos;
    }

    public SimpleTableInfo getTable(String db, String table) {
        try {
            Schema schema = openmldbSqlExecutor.getTableSchema(db, table);
            String schemaString = schema.toString();
            SimpleTableInfo simpleTableInfo = new SimpleTableInfo(db, table, schemaString);
            return simpleTableInfo;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}