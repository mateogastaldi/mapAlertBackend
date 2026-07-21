package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.UserResponseDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.service.AdministradorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// Todo lo que cuelga de /api/admin/** ya exige ROLE_ADMIN (ver SecurityConfig),
// por lo que solo un administrador autenticado puede crear a otro.
@RestController
@RequestMapping("/api/admin/administradores")
@RequiredArgsConstructor
public class AdministradorController {

    private final AdministradorService administradorService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> crearAdministrador(@Valid @RequestBody RegisterRequestDTO dto) {
        Usuario usuario = administradorService.crearAdministrador(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(usuario));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarAdministradores() {
        List<UserResponseDTO> administradores = administradorService.listarAdministradores().stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(administradores);
    }

    private UserResponseDTO toResponseDTO(Usuario usuario) {
        return UserResponseDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsuario())
                .email(usuario.getEmail())
                .firstName(usuario.getNombres())
                .lastName(usuario.getApellidos())
                .role(usuario.getRol().name())
                .activo(usuario.getActivo())
                .build();
    }
}
