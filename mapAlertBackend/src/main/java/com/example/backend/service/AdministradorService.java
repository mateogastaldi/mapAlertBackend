package com.example.backend.service;

import java.util.List;

import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.entity.Usuario;

public interface AdministradorService {

    Usuario crearAdministrador(RegisterRequestDTO dto);

    List<Usuario> listarAdministradores();
}
