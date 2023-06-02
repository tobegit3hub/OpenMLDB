package com._4paradigm.openmldb.featureplatform.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TableService {

    private final Statement openmldbStatement;

    @Autowired
    public TableService(Statement openmldbStatement) {
        this.openmldbStatement = openmldbStatement;
    }

    public List<String> getTables() {

        ArrayList<String> tableNames = new ArrayList<>();
        String sql = "SHOW TABLES";

        try {
            openmldbStatement.execute(sql);

            ResultSet result = openmldbStatement.getResultSet();
            while (result.next()) {
                String tablename = result.getString(1);
                tableNames.add(tablename);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableNames;
    }

}