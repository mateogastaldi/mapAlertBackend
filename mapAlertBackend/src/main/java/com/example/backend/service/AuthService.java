package com.example.backend.service;


import com.example.backend.dto.JwtResponseDTO;
import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.UserResponseDTO;

public interface AuthService {

    JwtResponseDTO login(LoginRequestDTO request);
    JwtResponseDTO register(RegisterRequestDTO request);
    UserResponseDTO me(String username);
}