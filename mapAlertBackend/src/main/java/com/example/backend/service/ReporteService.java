package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.backend.dto.CalificacionRequestDTO;
import com.example.backend.dto.CalificacionResponseDTO;
import com.example.backend.dto.ReporteDTO;
import com.example.backend.dto.VotoResponseDTO;
import com.example.backend.entity.Usuario;
import com.example.backend.enums.TipoReporte;
import com.example.backend.enums.TipoVoto;

public interface ReporteService {

        ReporteDTO crearReporte(ReporteDTO dto, Usuario user);

        List<ReporteDTO> listarReportes();

        List<ReporteDTO> getReportsByBounds(Double southLat, Double westLng, Double northLat, Double eastLng);

        List<ReporteDTO> getReportsByFilters(Boolean soloMios, LocalDateTime desdeFecha, List<String> categorias,
                        Usuario user);

        List<ReporteDTO> getByBoundsAndFilters(Double southLat, Double northLat, Double westLng, Double eastLng,
                        Boolean soloMios, LocalDateTime desdeFecha, List<TipoReporte> categorias,
                        Usuario user);

        CalificacionResponseDTO calificarReporte(CalificacionRequestDTO dto, Usuario user);

        VotoResponseDTO registrarVoto(Long reporteId, Usuario user, TipoVoto tipoVoto);
}
