package com.example.backend.controller;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.UsuarioUpdateDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user/profile")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserProfileController {

    private final UsuarioService usuarioService;

    @PutMapping
    public ResponseEntity<Usuario> actualizarPerfil(
            Principal principal,
            @Valid @RequestBody UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioService.actualizarPerfil(principal.getName(), dto);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping
    public ResponseEntity<Void> darDeBajaCuenta(@AuthenticationPrincipal Usuario user) {
        usuarioService.darDeBajaUsuario(user.getId());
        return ResponseEntity.noContent().build();
    }
}
