package com.capstone.FileSharing.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeComtroller {
    
    @GetMapping
    @PreAuthorize("hasRole('USER')")
    String index() {
        return "index";
    }

}
