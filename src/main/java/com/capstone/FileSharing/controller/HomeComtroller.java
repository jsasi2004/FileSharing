package com.capstone.FileSharing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeComtroller {
    
    @GetMapping
    String index() {
        return "index";
    }

}
