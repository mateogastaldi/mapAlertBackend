package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.RegisterRequestDTO;
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
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(
            @Valid @RequestBody RegisterRequestDTO dto,
            @RequestParam Rol rol) {
        Usuario usuario = usuarioService.crearUsuarioConRol(dto, rol);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioUpdateDTO dto,
            @RequestParam Rol rol) {
        Usuario usuario = usuarioService.actualizarUsuario(id, dto, rol);
        return ResponseEntity.ok(usuario);
    }

    @RequestMapping(value = "/{id}/rol", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<Usuario> cambiarRol(
            @PathVariable Long id,
            @RequestParam Rol rol) {
        Usuario usuario = usuarioService.cambiarRol(id, rol);
        return ResponseEntity.ok(usuario);
    }

    @RequestMapping(value = "/{id}/estado", method = {RequestMethod.PATCH, RequestMethod.PUT})
    public ResponseEntity<Usuario> cambiarEstado(
            @PathVariable Long id,
            @RequestParam(required = false) Boolean activo) {
        Usuario usuario = usuarioService.cambiarEstado(id, activo);
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> darDeBajaUsuario(@PathVariable Long id) {
        usuarioService.darDeBajaUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
