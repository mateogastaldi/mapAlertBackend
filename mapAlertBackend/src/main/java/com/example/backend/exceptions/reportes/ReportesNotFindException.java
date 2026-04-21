package com.example.backend.exceptions.reportes;

public class ReportesNotFindException extends RuntimeException {
    public ReportesNotFindException(){
        super("No se ha podido encontrar reportes en la base de datos.");
    }
}
