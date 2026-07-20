package com.example.backend.service;

import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.UsuarioUpdateDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.enums.Rol;

import java.util.List;

public interface UsuarioService {
    Usuario crearUsuario(RegisterRequestDTO dto);
    Usuario login(LoginRequestDTO dto);
    
    List<Usuario> listarTodos();
    Usuario crearUsuarioConRol(RegisterRequestDTO dto, Rol rol);
    Usuario actualizarUsuario(Long id, UsuarioUpdateDTO dto, Rol rol);
    Usuario actualizarPerfil(String username, UsuarioUpdateDTO dto);
    Usuario cambiarRol(Long id, Rol rol);
    Usuario cambiarEstado(Long id, Boolean activo);
    void darDeBajaUsuario(Long id);
}
