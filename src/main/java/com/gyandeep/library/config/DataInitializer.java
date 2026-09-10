package com.gyandeep.library.config;

import com.gyandeep.library.model.Role;
import com.gyandeep.library.model.User;
import com.gyandeep.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.default-username}")
    private String adminUsername;

    @Value("${app.admin.default-password}")
    private String adminPassword;

    @Value("${app.admin.default-email}")
    private String adminEmail;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User(adminUsername, adminEmail, passwordEncoder.encode(adminPassword), Role.ROLE_ADMIN);
            admin.grantFullAccess();
            userRepository.save(admin);
            System.out.println("=================================================================");
            System.out.println(" Default ADMIN account created");
            System.out.println(" Username: " + adminUsername);
            System.out.println(" Password: " + adminPassword);
            System.out.println(" Please log in and change this password immediately.");
            System.out.println("=================================================================");
        }
    }
}
