package com._4paradigm.openmldb.featureplatform.controller;

import com._4paradigm.openmldb.featureplatform.dao.OpenmldbDbService;
import com._4paradigm.openmldb.featureplatform.dao.SqlRequest;
import com._4paradigm.openmldb.featureplatform.utils.ResultSetUtil;
import com._4paradigm.openmldb.jdbc.SQLResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/sql")
public class SqlController {

    private final OpenmldbDbService openmldbDbService;

    @Autowired
    public SqlController(OpenmldbDbService openmldbDbService) {
        this.openmldbDbService = openmldbDbService;
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateSql(@RequestBody SqlRequest sqlRequest) {
        System.out.println("Try to validate sql: " + sqlRequest.getSql());
        // TODO: Validate sql
        return new ResponseEntity<>("Success to validate", HttpStatus.OK);
    }

    @PostMapping("/execute")
    public ResponseEntity<String> execute(@RequestBody SqlRequest sqlRequest) {
        try {
            SQLResultSet resultSet = openmldbDbService.executeSql(sqlRequest.getSql());
            String responseMessage = "Success to execute sql: " + sqlRequest.getSql();
            if (resultSet != null) {
                responseMessage = ResultSetUtil.resultSetToString(resultSet);
                System.out.println("Result set to string: " + responseMessage);
            }
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>("Fail to execute sql and get exception: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}