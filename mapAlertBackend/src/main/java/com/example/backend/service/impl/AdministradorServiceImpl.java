package com.example.backend.service.impl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.entity.Administrador;
import com.example.backend.entity.Usuario;
import com.example.backend.enums.Rol;
import com.example.backend.repository.AdministradorRepository;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.AdministradorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdministradorServiceImpl implements AdministradorService {

    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario crearAdministrador(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByUsuario(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .usuario(dto.getUsername())
                .contrasena(passwordEncoder.encode(dto.getPassword()))
                .nombres(dto.getFirstName())
                .apellidos(dto.getLastName())
                .email(dto.getEmail())
                .rol(Rol.ADMIN)
                .activo(true)
                .build();

        Administrador administrador = new Administrador();
        administrador.setUsuario(usuario);
        usuario.setAdministrador(administrador);

        return usuarioRepository.save(usuario);
    }

    @Override
    public List<Usuario> listarAdministradores() {
        return administradorRepository.findAll().stream()
                .map(Administrador::getUsuario)
                .toList();
    }
}
