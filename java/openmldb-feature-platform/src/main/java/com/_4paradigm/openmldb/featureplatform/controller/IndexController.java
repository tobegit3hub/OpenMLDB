package com._4paradigm.openmldb.featureplatform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
class IndexController {

    @GetMapping
    public String hello() {
        // TODO: Render the single page app
        return "Hello, World!";
    }
}