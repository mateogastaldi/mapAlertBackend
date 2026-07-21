package com.example.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "administradores")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Administrador {

    @Id
    @Column(name = "id_usuario")
    private Long idUsuario; // utiliza el mismo id que el usuario correspondiente

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // esto es para utilizar el mismo id que el usuario correspondiente
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    private Usuario usuario;

}
