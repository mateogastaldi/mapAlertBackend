package com.example.backend.exceptions.reportes;

public class ReporteNotSaveException extends RuntimeException {
    public ReporteNotSaveException(){
        super("No se pudo guardar correctamente el reporte en la base de datos.");
    }
}
