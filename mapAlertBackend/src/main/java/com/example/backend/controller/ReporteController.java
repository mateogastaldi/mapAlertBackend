package com.example.backend.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.CalificacionRequestDTO;
import com.example.backend.dto.CalificacionResponseDTO;
import com.example.backend.dto.ReporteDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.enums.TipoReporte;
import com.example.backend.service.ReporteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/reportes")
@CrossOrigin(origins = "http://localhost:5174") // después lo ajustamos
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService){
        this.reporteService = reporteService;
    }

    @PostMapping("/crear")
    public ResponseEntity<ReporteDTO> crearReporte(
            @Valid @RequestBody ReporteDTO dto,
            @AuthenticationPrincipal Usuario user) {

        ReporteDTO reporte = reporteService.crearReporte(dto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(reporte);
    }

    @GetMapping("/bounds")
    public ResponseEntity<List<ReporteDTO>> listarReportesByBounds(
            @RequestParam Double southLat,
            @RequestParam Double northLat,
            @RequestParam Double westLng,
            @RequestParam Double eastLng) {
        List<ReporteDTO> reports = reporteService.getReportsByBounds(southLat, northLat, westLng, eastLng);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/filters")
    public ResponseEntity<List<ReporteDTO>> listarReportesByBoundsAndFilters(
            @AuthenticationPrincipal Usuario user,
            @RequestParam(required = false) Boolean soloMios,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desdeFecha,
            @RequestParam(required = false) List<TipoReporte> categorias,
            @RequestParam Double southLat,
            @RequestParam Double northLat,
            @RequestParam Double westLng,
            @RequestParam Double eastLng) {
        System.out.println(southLat);
        List<ReporteDTO> reports = reporteService.getByBoundsAndFilters(southLat, northLat, westLng, eastLng, soloMios,
                desdeFecha, categorias, user);
        return ResponseEntity.ok(reports);
    }

    @PostMapping("/{reporteId}/calificar")
    public ResponseEntity<CalificacionResponseDTO> calificarReporte(
            @PathVariable Long reporteId,
            @Valid @RequestBody CalificacionRequestDTO dto,
            @AuthenticationPrincipal Usuario user) {

        CalificacionRequestDTO dtoFinal = new CalificacionRequestDTO(dto.getPuntaje(), reporteId);
        CalificacionResponseDTO result = reporteService.calificarReporte(dtoFinal, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
    

}
