package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.backend.entity.VotoReporte;
import com.example.backend.enums.TipoVoto;

public interface VotoReporteRepository extends JpaRepository<VotoReporte, Long> {
    Optional<VotoReporte> findByUsuarioIdAndReporteId(Long usuarioId, Long reporteId);

    @Query("SELECT COUNT(v) FROM VotoReporte v WHERE v.reporte.id = :reporteId AND v.tipoVoto = :tipo")
    long countByReporteIdAndTipoVoto(@Param("reporteId") Long reporteId, @Param("tipo") TipoVoto tipo);

}
