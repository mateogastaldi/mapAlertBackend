package com.example.backend.entity;
import java.time.LocalDateTime;

import com.example.backend.enums.TipoReporte;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@Table(name = "reporte")
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long id;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "calle")
    private String calle;

    @Column(name = "numero_calle")
    private Integer numeroCalle;

    @Column(name = "ciudad")
    private String ciudad;

    @Column(name = "provincia")
    private String provincia;

    @Column(name = "pais")
    private String pais;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reporte", nullable = false)
    private TipoReporte tipoReporte;

    @Column(name = "descripcion")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

}