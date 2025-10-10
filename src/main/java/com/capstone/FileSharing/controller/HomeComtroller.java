package com.capstone.FileSharing.controller;

import com.capstone.FileSharing.config.security.CustomUserDetails;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeComtroller {

    @GetMapping("/")
    @PreAuthorize("hasAuthority('USER')")
    public String index(Authentication authentication) {
        var principal = authentication.getPrincipal();
        CustomUserDetails customUserDetails = (CustomUserDetails) principal;
        if("USER".equals(customUserDetails.getRole())){
            return "index.html";
        } else if ("ADMIN".equals(customUserDetails.getRole())) {
            System.out.println("Admin trying to acce3sss");
            return "redirect:/admin";
        }
        return "redirect:/logout";
    }

}
