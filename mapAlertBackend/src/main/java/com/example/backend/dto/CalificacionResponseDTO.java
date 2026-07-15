package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CalificacionResponseDTO {
    private Long id;
    private Integer puntaje;
    private Long usuarioId;
    private Long reporteId;

}
