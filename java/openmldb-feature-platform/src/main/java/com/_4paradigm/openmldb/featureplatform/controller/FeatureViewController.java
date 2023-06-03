package com._4paradigm.openmldb.featureplatform.controller;

import com._4paradigm.openmldb.featureplatform.dao.FeatureView;
import com._4paradigm.openmldb.featureplatform.dao.FeatureViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/featureviews")
public class FeatureViewController {

    private final FeatureViewService featureViewService;

    @Autowired
    public FeatureViewController(FeatureViewService featureViewService) {
        this.featureViewService = featureViewService;
    }

    @GetMapping
    public List<FeatureView> getFeatureViews() {
        return featureViewService.getFeatureViews();
    }

    @GetMapping("/{name}")
    public FeatureView getFeatureViewByName(@PathVariable String name) {
        return featureViewService.getFeatureViewByName(name);
    }

    @PostMapping
    public FeatureView addFeatureView(@RequestBody FeatureView featureView) {
        if(featureViewService.addFeatureView(featureView)) {
            return featureView;
        } else {
            return null;
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<String> deleteFeatureView(@PathVariable String name) {
        if (featureViewService.deleteFeatureView(name)) {
            return new ResponseEntity<>("Success to delete", HttpStatus.OK);
        } else {
            // TODO: Handle for different error code
            return new ResponseEntity<>("Success to delete", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}