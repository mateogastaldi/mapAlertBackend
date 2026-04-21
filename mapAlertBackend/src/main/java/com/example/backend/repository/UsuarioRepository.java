package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);

    Optional<Usuario> findByUsuario(String usuario);
    //para verificar q no este ya registrado

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findById(Long id);
}
