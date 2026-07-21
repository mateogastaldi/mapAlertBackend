package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.UserResponseDTO;
import com.example.backend.dto.UsuarioUpdateDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.enums.Rol;
import com.example.backend.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/usuarios")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UsuarioAdminController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> listarTodos() {
        List<UserResponseDTO> usuarios = usuarioService.listarTodos().stream()
                .map(this::toResponseDTO)
                .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> crearUsuario(
            @Valid @RequestBody RegisterRequestDTO dto,
            @RequestParam Rol rol) {
        Usuario usuario = usuarioService.crearUsuarioConRol(dto, rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO dto,
            @RequestParam Rol rol) {
        Usuario usuario = usuarioService.actualizarUsuario(id, dto, rol);
        return ResponseEntity.ok(toResponseDTO(usuario));
    }

    @RequestMapping(value = "/{id}/rol", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<UserResponseDTO> cambiarRol(
            @PathVariable Long id,
            @RequestParam Rol rol) {
        Usuario usuario = usuarioService.cambiarRol(id, rol);
        return ResponseEntity.ok(toResponseDTO(usuario));
    }

    @RequestMapping(value = "/{id}/estado", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<UserResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean activo) {
        Usuario usuario = usuarioService.cambiarEstado(id, activo);
        return ResponseEntity.ok(toResponseDTO(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBajaUsuario(@PathVariable Long id) {
        usuarioService.darDeBajaUsuario(id);
        return ResponseEntity.noContent().build();
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
