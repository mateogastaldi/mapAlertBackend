package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.backend.exceptions.reportes.*;

@RestControllerAdvice
//El RestControllerAdvice maneja todas las excepciones lanzadas en los controllers
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ReporteNotSaveException.class)
    public ResponseEntity<String> handleReporteNotSave(ReporteNotSaveException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    };
    
    @ExceptionHandler(ReporteNotFindException.class)
    public ResponseEntity<String> handleReporteNotFind(ReporteNotFindException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    };

    @ExceptionHandler(ReportesNotFindException.class)
    public ResponseEntity<String> handleReportesNotFind(ReportesNotFindException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
