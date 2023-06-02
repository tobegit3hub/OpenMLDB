package com._4paradigm.openmldb.featureplatform;

import java.sql.SQLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com._4paradigm.openmldb.featureplatform.dao.OpenmldbDbService;

@SpringBootApplication
public class HttpServer {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(HttpServer.class, args);

        OpenmldbDbService openmldbDbService = context.getBean(OpenmldbDbService.class);
        try {
            // Init the OpenMLDB system tables
            openmldbDbService.initDbAndTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // SpringApplication.run(HttpServer.class, args);
    }
}
