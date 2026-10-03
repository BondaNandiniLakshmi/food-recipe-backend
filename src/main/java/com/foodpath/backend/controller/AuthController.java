package com.foodpath.backend.controller;

import com.foodpath.backend.entity.User;
import com.foodpath.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {

        try {

            if (request.fullname() == null ||
                    request.fullname().isBlank()) {

                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Full name is required"));
            }

            if (request.email() == null ||
                    request.email().isBlank()) {

                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email is required"));
            }

            if (request.password() == null ||
                    request.password().isBlank()) {

                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Password is required"));
            }

            if (request.role() == null ||
                    request.role().isBlank()) {

                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Role is required"));
            }

            User user = authService.signup(
                    request.fullname(),
                    request.email(),
                    request.password(),
                    request.role()
            );

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Signup successful",
                            "id", user.getId(),
                            "fullname", user.getFullname(),
                            "email", user.getEmail(),
                            "role", user.getRole()
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity.badRequest()
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {

            User user = authService.login(
                    request.email(),
                    request.password()
            );

            /*
             * Temporary token for our first working version.
             * We will replace this with proper JWT authentication
             * after login/signup is working.
             */
            String token = UUID.randomUUID().toString();

            return ResponseEntity.ok(
                    Map.of(
                            "message", "Login successful",
                            "token", token,
                            "id", user.getId(),
                            "fullname", user.getFullname(),
                            "email", user.getEmail(),
                            "role", user.getRole()
                    )
            );

        } catch (RuntimeException e) {

            return ResponseEntity.status(401)
                    .body(Map.of(
                            "message",
                            e.getMessage()
                    ));
        }
    }

    public record SignupRequest(
            String fullname,
            String email,
            String password,
            String role
    ) {
    }

    public record LoginRequest(
            String email,
            String password
    ) {
    }
}