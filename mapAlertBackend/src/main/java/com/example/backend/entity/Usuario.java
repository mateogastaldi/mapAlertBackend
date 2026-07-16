package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.backend.enums.Rol;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario"})})
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    protected Long id;

    @Column(name = "usuario", unique = true, nullable = false)
    protected String usuario;

    @Column(name = "contrasena")
    protected String contrasena;

    @Column(name = "nombres")
    protected String nombres;

    @Column(name = "apellidos")
    protected String apellidos;

    @Column(name = "email", unique = true, nullable = false)
    protected String email;

    @Column(name = "rol")
    protected Rol rol; 

    @Column(name = "activo", nullable = false)
    @Builder.Default
    protected Boolean activo = true;

    @Column(name = "fecha_creacion")
    protected LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now(); // se asigna automáticamente al guardar
    }

    // ── UserDetails (requerido por Spring Security) ──────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getPassword() {
        return contrasena; // Spring Security busca getPassword()
    }

    @Override
    public String getUsername() {
        return usuario; // usamos email como identificador único para el login
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return activo != null && activo; }

}