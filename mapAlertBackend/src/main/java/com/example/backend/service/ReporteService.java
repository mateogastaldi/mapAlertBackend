package com.example.backend.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.backend.dto.ReporteDTO;
import com.example.backend.entity.Reporte;


public interface ReporteService {

    ReporteDTO crearReporte(ReporteDTO dto);
    List<ReporteDTO> listarReportes();
    List<ReporteDTO> getReportsByBounds(Double southLat, Double westLng, Double northLat, Double eastLng);
}
