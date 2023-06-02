package com._4paradigm.openmldb.featureplatform.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class OpenmldbStatementBean {

    @Autowired
    private Environment env;

    @Bean
    public Statement openmldbStatement() {
        // Read config from yaml file
        String zkHost = env.getProperty("openmldb.zk_cluster");
        String zkPath = env.getProperty("openmldb.zk_path");

        Statement stmt = null;

        try {
            Class.forName("com._4paradigm.openmldb.jdbc.SQLDriver");
            System.out.println("Try to connect OpenMLDB with zk host: " + zkHost + ", path: " + zkPath);
            Connection connection = DriverManager.getConnection("jdbc:openmldb:///?zk=" + zkHost + "&zkPath=" + zkPath);
            stmt = connection.createStatement();

            stmt.execute("SET @@execute_mode='online'");
            stmt.execute("use SYSTEM_FEATURE_PLATFORM");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stmt;
    }
}
