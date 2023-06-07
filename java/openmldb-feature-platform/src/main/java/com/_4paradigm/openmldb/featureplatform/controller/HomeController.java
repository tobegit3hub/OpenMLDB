package com._4paradigm.openmldb.featureplatform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class HomeController {

    @GetMapping("/")
    public String index() {
        // TODO: Render the single page app
        return "index.html";
    }
}