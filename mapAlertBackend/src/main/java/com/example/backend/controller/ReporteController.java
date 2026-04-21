package com.example.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.ReporteDTO;
import com.example.backend.service.ReporteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reportes")
@CrossOrigin(origins = "http://localhost:5174") // después lo ajustamos
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping("/crear")
    public ResponseEntity<ReporteDTO> crearReporte(
            @Valid @RequestBody ReporteDTO dto) {

        ReporteDTO reporte = reporteService.crearReporte(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
    }

    @GetMapping("/bounds")
    public ResponseEntity<List<ReporteDTO>> listarReportesByBounds(
            @RequestParam Double southLat,
            @RequestParam Double northLat,
            @RequestParam Double westLng,
            @RequestParam Double eastLng){
        List<ReporteDTO> reports = reporteService.getReportsByBounds(southLat, northLat, westLng, eastLng);
        return ResponseEntity.ok(reports);
    }

    
}
