package com.example.backend.service;

import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.entity.Usuario;


public interface UsuarioService {
    Usuario crearUsuario(RegisterRequestDTO dto);
    Usuario login(LoginRequestDTO dto);
}



