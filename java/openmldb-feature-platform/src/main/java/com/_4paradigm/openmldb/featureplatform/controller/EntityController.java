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
    public List<Entity> getEntities() {
        return entityService.getEntities();
    }

    @GetMapping("/{name}")
    public Entity getEntityByName(@PathVariable String name) {
        return entityService.getEntityByName(name);
    }

    @PostMapping
    public Entity addEntity(@RequestBody Entity user) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteEntity(@PathVariable Long id) {
    }

}