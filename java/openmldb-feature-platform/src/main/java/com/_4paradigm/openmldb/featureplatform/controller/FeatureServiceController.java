package com._4paradigm.openmldb.featureplatform.controller;

import com._4paradigm.openmldb.featureplatform.dao.DataRequest;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public FeatureService addFeatureService(@RequestBody FeatureService featureService) {
        if(featureServiceService.addFeatureService(featureService)) {
            return featureService;
        } else {
            return null;
        }
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

}