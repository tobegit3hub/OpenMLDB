package com._4paradigm.openmldb.featureplatform.controller;

import com._4paradigm.openmldb.featureplatform.dao.model.FeatureServiceDeploymentRequest;
import com._4paradigm.openmldb.sdk.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import com._4paradigm.openmldb.featureplatform.dao.model.FeatureService;
import com._4paradigm.openmldb.featureplatform.dao.FeatureServiceService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/featureservices")
public class FeatureServiceController {

    private final FeatureServiceService featureServiceService;

    @Autowired
    public FeatureServiceController(FeatureServiceService featureServiceService) {
        this.featureServiceService = featureServiceService;
    }

    @GetMapping
    public List<FeatureService> getFeatureServices() {
        return featureServiceService.getFeatureServices();
    }

    @GetMapping("/{name}")
    public FeatureService getFeatureServiceByName(@PathVariable String name) {
        return featureServiceService.getFeatureServiceByName(name);
    }

    @PostMapping
    public FeatureService createFeatureService(@RequestBody FeatureService featureService) {
        return featureServiceService.createFeatureService(featureService);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteFeatureService(@PathVariable String name) {
        if (featureServiceService.deleteFeatureService(name)) {
            return new ResponseEntity<>("Success to delete", HttpStatus.OK);
        } else {
            // TODO: Handle for different error code
            return new ResponseEntity<>("Success to delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/{name}/request", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> requestFeatureService(@PathVariable String name, @RequestBody String dataRequest) {
        try {
            return featureServiceService.requestFeatureService(name, dataRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/deployments")
    public FeatureService createFeatureServiceFromDeployment(@RequestBody FeatureServiceDeploymentRequest request) {
        return featureServiceService.createFeatureServiceFromDeployment(request);
    }

    @GetMapping("/{name}/request/schema")
    public ResponseEntity<String> getRequestSchema(@PathVariable String name) {
        try {
            Schema schema = featureServiceService.getRequestSchema(name);
            return new ResponseEntity<>(schema.toString(), HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>("Success to delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{name}/tables")
    public List<String> getFeatureServiceDependentTables(@PathVariable String name) {
        try {
            return featureServiceService.getDependentTables(name);
        } catch (SQLException e) {
            return null;
        }
    }
}