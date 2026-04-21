package com.example.backend.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.service.UsuarioService;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // REGISTRO
    @PostMapping("/register")
    public ResponseEntity<Usuario> register(
            @Valid @RequestBody RegisterRequestDTO dto) {

        Usuario usuario = usuarioService.crearUsuario(dto);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<Usuario> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        Usuario usuario = usuarioService.login(dto);
        return ResponseEntity.ok(usuario);
    }
}
