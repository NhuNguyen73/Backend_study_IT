package com.cmcu.itstudy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmcu.itstudy.dto.request.LoginRequest;
import com.cmcu.itstudy.dto.request.RegisterRequest;
import com.cmcu.itstudy.dto.response.LoginResponse;
import com.cmcu.itstudy.dto.response.MeResponse;
import com.cmcu.itstudy.dto.response.RegisterResponse;
import com.cmcu.itstudy.entity.User;
import com.cmcu.itstudy.service.contract.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        MeResponse response = new MeResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName()
        );

        return ResponseEntity.ok(response);
    }
}

