package com._4paradigm.openmldb.featureplatform.controller;

import com._4paradigm.openmldb.featureplatform.dao.DatabaseService;
import com._4paradigm.openmldb.featureplatform.dao.model.SimpleTableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/databases")
public class DatabaseController {

    private final DatabaseService databaseService;

    @Autowired
    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @GetMapping
    public List<String> getDatabases() {
        return databaseService.getDatabases();
    }

    @GetMapping("/{db}")
    public List<SimpleTableInfo> getDatabaseTables(@PathVariable String db) {
        return databaseService.getDatabaseTables(db);
    }

}