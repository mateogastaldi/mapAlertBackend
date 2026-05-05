package com.example.backend.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Reporte;
import com.example.backend.enums.TipoReporte;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
        @Query("SELECT r FROM Reporte r WHERE " +
                        "r.activo = true AND " +
                        "r.latitud BETWEEN :southLat AND :northLat AND " +
                        "r.longitud BETWEEN :westLng AND :eastLng")
        List<Reporte> findByBounds(
                        @Param("southLat") Double southLat,
                        @Param("northLat") Double northLat,
                        @Param("westLng") Double westLng,
                        @Param("eastLng") Double eastLng);

        @Query("SELECT r FROM Reporte r WHERE " +
                        "r.usuario.id = :userId AND " +
                        "r.activo = true")
        List<Reporte> getSoloMio(@Param("user") Long userId);

        @Query("SELECT r FROM Reporte r WHERE " +
                        "r.activo = true AND " +
                        "r.usuario.id = :userId AND " +
                        "r.fechaCreacion >= :desdeFecha")
        List<Reporte> getSoloMioDesdeFecha(Long userId, LocalDateTime desdeFecha);

        @Query("SELECT r FROM Reporte r WHERE " +
                        "r.usuario.id = :userId AND " +
                        "r.activo = true AND " +
                        "r.fechaCreacion >= :desdeFecha AND " +
                        "r.tipoReporte IN :categorias")
        List<Reporte> getAllFilters(Long userId, LocalDateTime desdeFecha, List<String> categorias);

        @Query("SELECT r FROM Reporte r WHERE " +
                        "r.activo = true AND " +
                        "r.fechaCreacion >= :desdeFecha")
        List<Reporte> getDesdeFecha(LocalDateTime desdeFecha);

        @Query("SELECT r FROM Reporte r WHERE " +
                        "r.activo = true AND " +
                        "r.fechaCreacion >= :desdeFecha AND " +
                        "r.tipoReporte IN :categorias")
        List<Reporte> getDesdeFechaCategorias(LocalDateTime desdeFecha, List<String> categorias);

        @Query("SELECT r FROM Reporte r WHERE " +
                        "r.activo = true")
        List<Reporte> getAll();

        @Query("SELECT r FROM Reporte r WHERE " +
                        "r.latitud BETWEEN :latSur AND :latNorte AND " +
                        "r.longitud BETWEEN :lngOeste AND :lngEste AND " +
                        "r.activo = true AND " +
                        "(:userId IS NULL OR r.usuario.id = :userId) AND " +
                        "(:desdeFecha IS NULL OR r.fechaCreacion >= :desdeFecha) AND " +
                        "(:#{#categorias == null || #categorias.isEmpty()} = true OR r.tipoReporte IN :categorias)")
        List<Reporte> getByBoundsAndFilters(
                        @Param("latSur") Double latSur,
                        @Param("latNorte") Double latNorte,
                        @Param("lngOeste") Double lngOeste,
                        @Param("lngEste") Double lngEste,
                        @Param("userId") Long userId,
                        @Param("desdeFecha") LocalDateTime desdeFecha,
                        @Param("categorias") List<TipoReporte> categorias);
}
