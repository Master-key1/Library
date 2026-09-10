package com.gyandeep.library.controller;

import com.gyandeep.library.model.User;
import com.gyandeep.library.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String viewProfile(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@RequestParam String phone,
                                 @RequestParam String address,
                                 Authentication authentication,
                                 Model model) {
        User user = userService.findByUsername(authentication.getName());
        userService.updateProfile(user.getId(), phone, address);
        model.addAttribute("success", "Profile updated successfully.");
        model.addAttribute("user", userService.findByUsername(authentication.getName()));
        return "profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String newPassword,
                                  @RequestParam String confirmPassword,
                                  Authentication authentication,
                                  Model model) {
        User user = userService.findByUsername(authentication.getName());
        if (newPassword == null || newPassword.length() < 4) {
            model.addAttribute("error", "Password must be at least 4 characters long.");
        } else if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
        } else {
            userService.updatePassword(user.getId(), newPassword);
            model.addAttribute("success", "Password changed successfully.");
        }
        model.addAttribute("user", userService.findByUsername(authentication.getName()));
        return "profile";
    }
}
