package com._4paradigm.openmldb.featureplatform.controller;

import com._4paradigm.openmldb.featureplatform.dao.OpenmldbDbService;
import com._4paradigm.openmldb.featureplatform.dao.SqlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/query")
    public ResponseEntity<String> query(@RequestBody SqlRequest sqlRequest) {
        System.out.println("Try to query sql: " + sqlRequest.getSql());
        openmldbDbService.querySql(sqlRequest.getSql());
        return new ResponseEntity<>("Success to validate", HttpStatus.OK);
    }

    @PostMapping("/execute")
    public ResponseEntity<String> execute(@RequestBody SqlRequest sqlRequest) {
        System.out.println("Try to execute sql: " + sqlRequest.getSql());
        openmldbDbService.executeSql(sqlRequest.getSql());
        return new ResponseEntity<>("Success to validate", HttpStatus.OK);
    }

    @PostMapping("/request_query")
    public ResponseEntity<String> requestQuery(@RequestBody SqlRequest sqlRequest) {
        System.out.println("Try to query sql in request mode: " + sqlRequest.getSql());
        // TODO: query in request mode
        return new ResponseEntity<>("Success to validate", HttpStatus.OK);
    }

}