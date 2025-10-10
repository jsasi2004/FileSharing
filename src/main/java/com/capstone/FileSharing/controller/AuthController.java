package com.capstone.FileSharing.controller;

import com.capstone.FileSharing.config.security.CustomUserDetails;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    String getLoginPage(Authentication authentication) {
        if(authentication!=null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)){
            if(authentication.getPrincipal()!=null){
                String role = ((CustomUserDetails) authentication.getPrincipal()).getRole();
                if("USER".equals(role)){
                    return "redirect:/";
                }
                if("ADMIN".equals(role)){
                    return "redirect:/admin";
                }
            }
        }
        return "auth/login";

    }

    @GetMapping("/admin")
    String getAdminPage(){
        return "auth/admin";
    }


}
