package com._4paradigm.openmldb.featureplatform.controller;

import com._4paradigm.openmldb.featureplatform.dao.EntityService;
import com._4paradigm.openmldb.featureplatform.dao.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/entities")
public class EntityController {

    private final EntityService entityService;

    @Autowired
    public EntityController(EntityService entityService) {
        this.entityService = entityService;
    }

    @GetMapping
    public List<Entity> test() {
        entityService.test();
        return null;
    }

    @GetMapping("/{id}")
    public Entity getEntityByName(@PathVariable String name) {
        return null;
    }

    @PostMapping
    public Entity addEntity(@RequestBody Entity user) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteEntity(@PathVariable Long id) {
    }

}