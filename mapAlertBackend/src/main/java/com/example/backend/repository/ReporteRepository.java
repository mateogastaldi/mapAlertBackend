package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Reporte;


@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    // por ahora nada más
    //con esto ya puedo hacer save, findAll, findById, deleteById, etc
    @Query("SELECT r FROM Reporte r WHERE " +
           "r.latitud BETWEEN :southLat AND :northLat AND " +
           "r.longitud BETWEEN :westLng AND :eastLng")
    List<Reporte> findByBounds(
        @Param("southLat") Double southLat,
        @Param("northLat") Double northLat,
        @Param("westLng")  Double westLng,
        @Param("eastLng")  Double eastLng
    );
}
