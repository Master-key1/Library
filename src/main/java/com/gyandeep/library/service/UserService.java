package com.gyandeep.library.service;

import com.gyandeep.library.model.Role;
import com.gyandeep.library.model.User;
import com.gyandeep.library.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User registerNewUser(String username, String email, String rawPassword) {
        User user = new User(username, email, passwordEncoder.encode(rawPassword), Role.ROLE_USER);
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void updateAccessRights(Long userId, boolean manageAdmissions, boolean manageBooks,
                                    boolean manageUsers, boolean viewReports, boolean enabled) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setCanManageAdmissions(manageAdmissions);
        user.setCanManageBooks(manageBooks);
        user.setCanManageUsers(manageUsers);
        user.setCanViewReports(viewReports);
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Transactional
    public void updatePassword(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updateProfile(Long userId, String phone, String address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPhone(phone);
        user.setAddress(address);
        userRepository.save(user);
    }
}
