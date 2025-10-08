package com.capstone.FileSharing.controller;

import com.capstone.FileSharing.model.User;
import com.capstone.FileSharing.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping
    public String getAdminPage(@ModelAttribute("user") User user) {
        return "auth/admin";
    }

    @PostMapping("/add-user")
    public String adduserController(@ModelAttribute("user") User user) {
        try {
            userService.register(user);
        } catch (Exception e) {
            return "redirect:/admin?error";
        }

        return "redirect:/admin?success";
    }

}
