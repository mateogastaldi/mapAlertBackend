package com.example.backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.parser.Entity;

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

        ReporteDTO reporteGuardadoDTO = new ReporteDTO().builder()
                .city(reporteGuardado.getCiudad())
                .country(reporteGuardado.getPais())
                .lat(reporteGuardado.getLatitud())
                .lng(reporteGuardado.getLongitud())
                .reportDescription(reporteGuardado.getDescripcion())
                .reportType(reporteGuardado.getTipoReporte())
                .state(reporteGuardado.getProvincia())
                .street(reporteGuardado.getCalle())
                .streetNumber(reporteGuardado.getNumeroCalle())
                .build();

        return reporteGuardadoDTO;
    }

    @Override
    public List<ReporteDTO> listarReportes() {
        List<Reporte> reportes;
        try {
            reportes = reporteRepository.findAll();
        } catch (Exception e) {
            throw new ReportesNotFindException();
        }

        List<ReporteDTO> reportesDTO = null;

        for (Reporte reporte : reportes) {
            ReporteDTO reporteDTO = new ReporteDTO().builder()
                    .city(reporte.getCiudad())
                    .country(reporte.getPais())
                    .lat(reporte.getLatitud())
                    .lng(reporte.getLongitud())
                    .reportDescription(reporte.getDescripcion())
                    .reportType(reporte.getTipoReporte())
                    .state(reporte.getProvincia())
                    .street(reporte.getCalle())
                    .streetNumber(reporte.getNumeroCalle())
                    .build();

            reportesDTO.add(reporteDTO);
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
            reportesDTO.add(new ReporteDTO().builder()
                    .city(reporte.getCiudad())
                    .country(reporte.getPais())
                    .lat(reporte.getLatitud())
                    .lng(reporte.getLongitud())
                    .reportDescription(reporte.getDescripcion())
                    .reportType(reporte.getTipoReporte())
                    .state(reporte.getProvincia())
                    .street(reporte.getCalle())
                    .streetNumber(reporte.getNumeroCalle())
                    .build());
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
            new ReporteDTO();
            reportesDTO.add(ReporteDTO.builder()
                    .city(reporte.getCiudad())
                    .country(reporte.getPais())
                    .lat(reporte.getLatitud())
                    .lng(reporte.getLongitud())
                    .reportDescription(reporte.getDescripcion())
                    .reportType(reporte.getTipoReporte())
                    .state(reporte.getProvincia())
                    .street(reporte.getCalle())
                    .streetNumber(reporte.getNumeroCalle())
                    .build());
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
                .map(reporte -> ReporteDTO.builder()
                        .city(reporte.getCiudad())
                        .country(reporte.getPais())
                        .lat(reporte.getLatitud())
                        .lng(reporte.getLongitud())
                        .reportDescription(reporte.getDescripcion())
                        .reportType(reporte.getTipoReporte())
                        .state(reporte.getProvincia())
                        .street(reporte.getCalle())
                        .streetNumber(reporte.getNumeroCalle())
                        .build())
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
        long confirmas = votoReporteRepository.countByReporteIdAndTipoVoto(reporte.getId(), TipoVoto.CONFIRMA);

        long votoNeto = niegas - confirmas;

        if (votoNeto >= umbralDesactivacion) {
            reporte.setActivo(false);
            reporteRepository.save(reporte);
        }
    }

}
