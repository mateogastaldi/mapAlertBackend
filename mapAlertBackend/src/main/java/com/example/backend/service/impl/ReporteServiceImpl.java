package com.example.backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.backend.dto.CalificacionRequestDTO;
import com.example.backend.dto.CalificacionResponseDTO;
import com.example.backend.dto.ReporteDTO;
import com.example.backend.dto.VotoResponseDTO;
import com.example.backend.entity.Calificacion;
import com.example.backend.entity.Reporte;
import com.example.backend.entity.Usuario;
import com.example.backend.entity.VotoReporte;
import com.example.backend.enums.TipoReporte;
import com.example.backend.enums.TipoVoto;
import com.example.backend.exceptions.reportes.ReporteNotSaveException;
import com.example.backend.exceptions.reportes.ReportesNotFindException;
import com.example.backend.repository.ReporteRepository;
import com.example.backend.repository.VotoReporteRepository;
import com.example.backend.repository.CalificacionRepository;
import com.example.backend.service.ReporteService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    @Value("${reporte.votos.umbral-desactivacion}")
    private int umbralDesactivacion;

    private final ReporteRepository reporteRepository;
    private final CalificacionRepository calificacionRepository;
    private final VotoReporteRepository votoReporteRepository;

    @Override
    public ReporteDTO crearReporte(ReporteDTO dto, Usuario user) {
        Reporte reporte = new Reporte();

        reporte.setLatitud(dto.getLat());
        reporte.setLongitud(dto.getLng());
        reporte.setCalle(dto.getStreet());
        reporte.setNumeroCalle(dto.getStreetNumber());
        reporte.setCiudad(dto.getCity());
        reporte.setProvincia(dto.getState());
        reporte.setPais(dto.getCountry());
        reporte.setTipoReporte(dto.getReportType());
        reporte.setDescripcion(dto.getReportDescription());
        reporte.setFechaCreacion(LocalDateTime.now());
        reporte.setActivo(true);
        reporte.setUsuario(user);

        Reporte reporteGuardado;

        try {
            reporteGuardado = reporteRepository.save(reporte);
        } catch (Exception e) {
            throw new ReporteNotSaveException();
        }

        return mapToDTO(reporteGuardado);
    }

    private ReporteDTO mapToDTO(Reporte reporte) {
        if (reporte == null) return null;

        long confirms = votoReporteRepository.countByReporteIdAndTipoVoto(reporte.getId(), TipoVoto.CONFIRMA);
        long dismisses = votoReporteRepository.countByReporteIdAndTipoVoto(reporte.getId(), TipoVoto.NIEGA);

        double avgRating = 0.0;
        if (reporte.getCalificaciones() != null && !reporte.getCalificaciones().isEmpty()) {
            avgRating = reporte.getCalificaciones().stream()
                    .mapToDouble(c -> c.getPuntaje())
                    .average()
                    .orElse(0.0);
        }

        return ReporteDTO.builder()
                .id(reporte.getId())
                .activo(reporte.getActivo())
                .usuarioId(reporte.getUsuario() != null ? reporte.getUsuario().getId() : null)
                .usuarioName(reporte.getUsuario() != null ? reporte.getUsuario().getUsuario() : null)
                .verificationCount(confirms)
                .dismissCount(dismisses)
                .averageRating(avgRating)
                .lat(reporte.getLatitud())
                .lng(reporte.getLongitud())
                .street(reporte.getCalle())
                .streetNumber(reporte.getNumeroCalle())
                .city(reporte.getCiudad())
                .state(reporte.getProvincia())
                .country(reporte.getPais())
                .reportType(reporte.getTipoReporte())
                .reportDescription(reporte.getDescripcion())
                .build();
    }

    @Override
    public List<ReporteDTO> listarReportes() {
        List<Reporte> reportes;
        try {
            reportes = reporteRepository.findAll();
        } catch (Exception e) {
            throw new ReportesNotFindException();
        }

        List<ReporteDTO> reportesDTO = new ArrayList<>();

        for (Reporte reporte : reportes) {
            reportesDTO.add(mapToDTO(reporte));
        }

        return reportesDTO;
    }

    public List<ReporteDTO> getReportsByBounds(Double southLat, Double northLat, Double westLng, Double eastLng) {
        List<Reporte> reportes;
        try {
            reportes = reporteRepository.findByBounds(southLat, northLat, westLng, eastLng);
        } catch (Exception e) {
            throw new ReportesNotFindException();
        }
        List<ReporteDTO> reportesDTO = new ArrayList<>();

        for (Reporte reporte : reportes) {
            reportesDTO.add(mapToDTO(reporte));
        }
        return reportesDTO;

    }

    public List<ReporteDTO> getReportsByFilters(Boolean soloMios, LocalDateTime desdeFecha, List<String> categorias,
            Usuario user) {
        List<Reporte> reportes;
        if (soloMios && soloMios != null && desdeFecha == null && (categorias.size() == 0 || categorias == null)) {
            reportes = reporteRepository.getSoloMio(user.getId());
        } else {
            if (soloMios && soloMios != null && desdeFecha != null && (categorias.size() == 0 || categorias == null)) {
                reportes = reporteRepository.getSoloMioDesdeFecha(user.getId(), desdeFecha);
            } else {
                if (soloMios && soloMios != null && desdeFecha != null && categorias.size() > 0) {
                    reportes = reporteRepository.getAllFilters(user.getId(), desdeFecha, categorias);
                } else {
                    if ((!soloMios || soloMios == null) && desdeFecha != null
                             && (categorias.size() == 0 || categorias == null)) {
                        reportes = reporteRepository.getDesdeFecha(desdeFecha);
                    } else {
                        if ((!soloMios || soloMios == null) && desdeFecha != null && categorias.size() > 0) {
                            reportes = reporteRepository.getDesdeFechaCategorias(desdeFecha, categorias);
                        } else {
                            reportes = reporteRepository.getAll();
                        }
                    }
                }
            }
        }

        List<ReporteDTO> reportesDTO = new ArrayList<>();
        for (Reporte reporte : reportes) {
            reportesDTO.add(mapToDTO(reporte));
        }

        return reportesDTO;
    }

    public List<ReporteDTO> getByBoundsAndFilters(Double southLat, Double northLat, Double westLng, Double eastLng,
            Boolean soloMios, LocalDateTime desdeFecha, List<TipoReporte> categorias, Usuario user) {

        Long idUser = null;
        if (soloMios != null && soloMios)
            idUser = user.getId();

        // Si la lista viene vacía la convertimos a null
        if (categorias != null && categorias.isEmpty())
            categorias = null;

        List<Reporte> reportes = reporteRepository.getByBoundsAndFilters(
                southLat, northLat, westLng, eastLng, idUser, desdeFecha, categorias);

        return reportes.stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public CalificacionResponseDTO calificarReporte(CalificacionRequestDTO dto, Usuario user) {
        Reporte reporte = reporteRepository.findById(dto.getReporteId())
                .orElseThrow(() -> new EntityNotFoundException("Reporte no encontrado"));

        calificacionRepository.findByReporteIdAndUsuarioId(dto.getReporteId(), user.getId())
                .ifPresent(c -> {
                    throw new IllegalStateException("El usuario ya registro una clasificacion para este reporte");
                });
        ;

        Calificacion calificacion = new Calificacion().builder()
                .puntaje(dto.getPuntaje())
                .usuario(user)
                .build();

        reporte.agregarCalificacion(calificacion);

        calificacionRepository.save(calificacion);

        return CalificacionResponseDTO.builder()
                .reporteId(calificacion.getReporte().getId())
                .usuarioId(calificacion.getUsuario().getId())
                .puntaje(calificacion.getPuntaje())
                .build();
    }

    @Override
    @Transactional
    public VotoResponseDTO registrarVoto(Long reporteId, Usuario user, TipoVoto tipoVoto) {
        Reporte reporte = reporteRepository.findById(reporteId)
                .orElseThrow(() -> new EntityNotFoundException("Reporte no encontrado: " + reporteId));

        if (!reporte.getActivo()) {
            throw new IllegalStateException("El reporte ya está inactivo, no se puede votar");
        }

        // Buscamos si el usuario ya votó antes en este reporte
        Optional<VotoReporte> votoExistente = votoReporteRepository.findByUsuarioIdAndReporteId(user.getId(),
                reporteId);

        if (votoExistente.isPresent()) {
            VotoReporte voto = votoExistente.get();
            if (voto.getTipoVoto() == tipoVoto) {
                // Ya votó exactamente lo mismo, no hacemos nada (evita spam de clicks)
                throw new IllegalStateException("Ya registraste este voto");
            }
            // Cambió de opinión: actualizamos el voto existente
            voto.setTipoVoto(tipoVoto);
            voto.setFechaVoto(LocalDateTime.now());
            votoReporteRepository.save(voto);
        } else {
            // Voto nuevo
            VotoReporte voto = VotoReporte.builder()
                    .tipoVoto(tipoVoto)
                    .fechaVoto(LocalDateTime.now())
                    .usuario(user)
                    .reporte(reporte)
                    .build();
            votoReporteRepository.save(voto);
        }

        evaluarDesactivacion(reporte);

        return new VotoResponseDTO(reporte.getId(), reporte.getActivo());
    }

    private void evaluarDesactivacion(Reporte reporte) {
        long niegas = votoReporteRepository.countByReporteIdAndTipoVoto(reporte.getId(), TipoVoto.NIEGA);

        if (niegas >= 5) {
            reporte.setActivo(false);
            reporte.setFechaDesactivacion(LocalDateTime.now());
            reporteRepository.save(reporte);
        }
    }

    @Override
    @Transactional
    public ReporteDTO actualizarReporte(Long id, ReporteDTO dto, Usuario user) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reporte no encontrado: " + id));

        if (!reporte.getUsuario().getId().equals(user.getId()) 
                && user.getRol() != com.example.backend.enums.Rol.ADMIN) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.FORBIDDEN, "No tiene permisos para modificar este reporte"
            );
        }

        reporte.setTipoReporte(dto.getReportType());
        reporte.setDescripcion(dto.getReportDescription());
        Reporte actualizado = reporteRepository.save(reporte);
        return mapToDTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminarReporte(Long id, Usuario user) {
        Reporte reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reporte no encontrado: " + id));

        if (!reporte.getUsuario().getId().equals(user.getId()) 
                && user.getRol() != com.example.backend.enums.Rol.ADMIN) {
            throw new org.springframework.web.server.ResponseStatusException(
                org.springframework.http.HttpStatus.FORBIDDEN, "No tiene permisos para eliminar este reporte"
            );
        }

        reporte.setActivo(false);
        reporte.setFechaDesactivacion(LocalDateTime.now());
        reporteRepository.save(reporte);
    }

}
