package com.foodpath.backend.service;

import com.foodpath.backend.entity.User;
import com.foodpath.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User signup(String fullname, String email, String password, String role) {

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFullname(fullname);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        return userRepository.save(user);
    }

    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }
}