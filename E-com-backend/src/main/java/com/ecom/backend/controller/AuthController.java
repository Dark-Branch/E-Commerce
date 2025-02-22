package com.ecom.backend.controller;

import com.ecom.backend.DTO.JwtResponse;
import com.ecom.backend.DTO.LoginRequest;
import com.ecom.backend.DTO.SignupRequest;
import com.ecom.backend.model.User;
import com.ecom.backend.service.AuthService;
import com.ecom.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        User registeredUser = authService.registerUser(signupRequest);
        return ResponseEntity.ok("User registered successfully");
    }
    // TODO: role selection logic

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.authenticateUser(loginRequest);
            return ResponseEntity.ok(new JwtResponse(token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @DeleteMapping // FIXME: does this belong to user controller or user controller
    public ResponseEntity<Void> removeUser(Principal principal){
        String currentUsername = principal.getName();
        if (currentUsername == null || currentUsername.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userService.deleteUser(currentUsername);
        return ResponseEntity.ok().build();
    }
}

// TODO: are we going to do soft delete
// TODO: make admins too can delete users
