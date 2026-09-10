package com.gyandeep.library.controller;

import com.gyandeep.library.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@ModelAttribute RegisterForm registerForm, Model model) {
        if (registerForm.getUsername() == null || registerForm.getUsername().isBlank()) {
            model.addAttribute("error", "Username is required.");
            return "register";
        }
        if (userService.usernameExists(registerForm.getUsername())) {
            model.addAttribute("error", "That username is already taken. Please choose another.");
            return "register";
        }
        if (userService.emailExists(registerForm.getEmail())) {
            model.addAttribute("error", "An account with this email already exists.");
            return "register";
        }
        if (registerForm.getPassword() == null || registerForm.getPassword().length() < 4) {
            model.addAttribute("error", "Password must be at least 4 characters long.");
            return "register";
        }
        if (!registerForm.getPassword().equals(registerForm.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        userService.registerNewUser(registerForm.getUsername(), registerForm.getEmail(), registerForm.getPassword());
        return "redirect:/login?registered=true";
    }

    public static class RegisterForm {
        @NotBlank
        private String username;
        @NotBlank @Email
        private String email;
        @NotBlank @Size(min = 4)
        private String password;
        private String confirmPassword;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    }
}
