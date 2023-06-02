package com._4paradigm.openmldb.featureplatform.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class EntityService {

    private final Statement openmldbStatement;

    @Autowired
    public EntityService(Statement openmldbStatement) {
        this.openmldbStatement = openmldbStatement;
    }

    public void test() {
        String sql = "select 100";

        try {
            openmldbStatement.execute(sql);
            if (sql.toLowerCase().startsWith("select")) {
                ResultSet result = openmldbStatement.getResultSet();
                while (result.next()) {
                    System.out.println(result.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}