package com.library.service;

import com.library.model.mysql.User;
import com.library.repository.mysql.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    // Register new user
    public User register(User user) {
        // Check username exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken: "
                    + user.getUsername());
        }

        // Check email exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered: "
                    + user.getEmail());
        }

        // Set default role
        if (user.getRole() == null) {
            user.setRole(User.Role.USER);
        }

        user.setActive(true);

        // Save user
        // Note: In production always encrypt password!
        // We will add encryption in security step
        return userRepository.save(user);
    }

    // Login user
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found: " + username));

        // Check password
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password!");
        }

        // Check if active
        if (!user.isActive()) {
            throw new RuntimeException("Account is disabled!");
        }

        return user;
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}