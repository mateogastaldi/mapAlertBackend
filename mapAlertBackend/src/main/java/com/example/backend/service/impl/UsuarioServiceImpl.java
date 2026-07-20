package com.example.backend.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend.dto.LoginRequestDTO;
import com.example.backend.dto.RegisterRequestDTO;
import com.example.backend.dto.UsuarioUpdateDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.enums.Rol;
import com.example.backend.repository.UsuarioRepository;
import com.example.backend.service.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario crearUsuario(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByUsuario(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsuario(dto.getUsername());
        usuario.setContrasena(passwordEncoder.encode(dto.getPassword()));
        usuario.setNombres(dto.getFirstName());
        usuario.setApellidos(dto.getLastName());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(Rol.USER);
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario login(LoginRequestDTO dto) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(dto.getUsername());

        if (usuarioOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getActivo() != null && !usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Esta cuenta ha sido dada de baja.");
        }

        if (!passwordEncoder.matches(dto.getPassword(), usuario.getContrasena())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
        }

        return usuario;
    }

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario crearUsuarioConRol(RegisterRequestDTO dto, Rol rol) {
        if (usuarioRepository.existsByUsuario(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
        }
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        if (dto.getPassword() == null || dto.getPassword().length() < 8 || !dto.getPassword().matches(".*[A-Z].*")) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La contraseña debe tener al menos 8 caracteres y contener al menos una letra mayúscula."
            );
        }

        Usuario usuario = Usuario.builder()
                .usuario(dto.getUsername())
                .contrasena(passwordEncoder.encode(dto.getPassword()))
                .nombres(dto.getFirstName())
                .apellidos(dto.getLastName())
                .email(dto.getEmail())
                .rol(rol != null ? rol : Rol.USER)
                .activo(true)
                .build();

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarUsuario(Long id, UsuarioUpdateDTO dto, Rol rol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (usuario.getActivo() != null && !usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede modificar una cuenta dada de baja.");
        }

        if (!usuario.getUsuario().equals(dto.getUsername()) && usuarioRepository.existsByUsuario(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
        }
        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        usuario.setUsuario(dto.getUsername());
        usuario.setNombres(dto.getFirstName());
        usuario.setApellidos(dto.getLastName());
        usuario.setEmail(dto.getEmail());

        if (rol != null) {
            usuario.setRol(rol);
        }

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty() && !dto.getPassword().equals("******")) {
            if (dto.getPassword().length() < 8 || !dto.getPassword().matches(".*[A-Z].*")) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres y contener al menos una letra mayúscula."
                );
            }
            usuario.setContrasena(passwordEncoder.encode(dto.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizarPerfil(String username, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        if (usuario.getActivo() != null && !usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Esta cuenta ha sido dada de baja.");
        }

        if (!usuario.getUsuario().equals(dto.getUsername()) && usuarioRepository.existsByUsuario(dto.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya existe");
        }
        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email ya está registrado");
        }

        usuario.setUsuario(dto.getUsername());
        usuario.setNombres(dto.getFirstName());
        usuario.setApellidos(dto.getLastName());
        usuario.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty() && !dto.getPassword().equals("******")) {
            if (dto.getPassword().length() < 8 || !dto.getPassword().matches(".*[A-Z].*")) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La contraseña debe tener al menos 8 caracteres y contener al menos una letra mayúscula."
                );
            }
            usuario.setContrasena(passwordEncoder.encode(dto.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario cambiarRol(Long id, Rol rol) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        if (usuario.getActivo() != null && !usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se puede cambiar el rol a una cuenta dada de baja.");
        }

        usuario.setRol(rol);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario cambiarEstado(Long id, Boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        if (usuario.getActivo() != null && !usuario.getActivo()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Una cuenta dada de baja no puede volver a activarse.");
        }

        usuario.setActivo(false); // Solo se permite desactivar / dar de baja permanente
        return usuarioRepository.save(usuario);
    }

    @Override
    public void darDeBajaUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }
}
