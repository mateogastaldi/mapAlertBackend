package com.example.backend.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.JwtResponseDTO;
import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.UserResponseDTO;
import com.example.backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO request){
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request){
        return ResponseEntity.ok(authService.register(request));
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Principal principal) {
        return ResponseEntity.ok(authService.me(principal.getName()));
    }
}