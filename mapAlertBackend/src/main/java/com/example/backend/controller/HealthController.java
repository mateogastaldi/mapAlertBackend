package com.example.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.HealthDTO;

@RestController
public class HealthController {

    @GetMapping("/health")
    public HealthDTO health() {
        return new HealthDTO(
                "OK",
                "Backend is running correctly");
    }
}
