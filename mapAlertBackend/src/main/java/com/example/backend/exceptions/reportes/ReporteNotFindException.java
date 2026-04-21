package com.example.backend.exceptions.reportes;

public class ReporteNotFindException extends RuntimeException{
    public ReporteNotFindException(Long id){
        super("No se pudo encontrar el reporte con id: " + id + "en la base de datos.");
    }
    
}
