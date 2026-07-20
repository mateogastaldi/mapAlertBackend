package com.example.backend.service.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.JwtResponseDTO;
import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.UserResponseDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.enums.Rol;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.AuthService;
import com.example.backend.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public JwtResponseDTO login(LoginRequestDTO request){
        Usuario userEntity = usuarioRepository.findByUsuario(request.getUsername()).orElse(null);
        if (userEntity != null && userEntity.getActivo() != null && !userEntity.getActivo()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Esta cuenta ha sido dada de baja.");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos.");
        }

        UserDetails user = usuarioRepository.findByUsuario(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        return JwtResponseDTO.builder().token(token).build();
    }

    public JwtResponseDTO register(RegisterRequestDTO request){
        if (usuarioRepository.existsByUsuario(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        if (request.getPassword() == null || request.getPassword().length() < 8 || !request.getPassword().matches(".*[A-Z].*")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La contraseña debe tener al menos 8 caracteres y contener al menos una letra mayúscula."
            );
        }

        Usuario user = Usuario.builder()
            .usuario(request.getUsername())
            .apellidos(request.getLastName())
            .contrasena(passwordEncoder.encode(request.getPassword()))
            .nombres(request.getFirstName())
            .rol(Rol.USER)
            .email(request.getEmail())
            .build();

        usuarioRepository.save(user);

        return JwtResponseDTO.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    public UserResponseDTO me(String username) {
        Usuario user = usuarioRepository.findByUsuario(username).orElseThrow();
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsuario())
                .email(user.getEmail())
                .firstName(user.getNombres())
                .lastName(user.getApellidos())
                .role(user.getRol().name())
                .build();
    }
}
