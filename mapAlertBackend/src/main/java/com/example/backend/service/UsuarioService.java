package com.example.backend.service;

import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.entity.Usuario;


public interface UsuarioService {
    Usuario crearUsuario(RegisterRequestDTO dto);
    Usuario login(LoginRequestDTO dto);
    
    java.util.List<Usuario> listarTodos();
    Usuario crearUsuarioConRol(RegisterRequestDTO dto, com.example.backend.enums.Rol rol);
    Usuario actualizarUsuario(Long id, RegisterRequestDTO dto, com.example.backend.enums.Rol rol);
    Usuario actualizarPerfil(String username, RegisterRequestDTO dto);
    void darDeBajaUsuario(Long id);
}



