package com.example.backend.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.ReporteDTO;
import com.example.backend.entity.Reporte;
import com.example.backend.exceptions.reportes.ReporteNotSaveException;
import com.example.backend.exceptions.reportes.ReportesNotFindException;
import com.example.backend.repository.ReporteRepository;
import com.example.backend.service.ReporteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;

    @Override
    public ReporteDTO crearReporte(ReporteDTO dto) {
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

        Reporte reporteGuardado;

        try{
            reporteGuardado = reporteRepository.save(reporte);
        } catch (Exception e){
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
        try{
            reportes = reporteRepository.findAll();
        } catch (Exception e){
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

    public List<ReporteDTO> getReportsByBounds(Double southLat, Double northLat, Double westLng , Double eastLng){
        List<Reporte> reportes;
        try{
            reportes = reporteRepository.findByBounds(southLat, northLat, westLng, eastLng);
        } catch (Exception e){
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
}
