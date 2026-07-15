package com.example.backend.dto;

import com.example.backend.enums.TipoVoto;

import jakarta.validation.constraints.NotNull;
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
public class VotoRequestDTO {
    @NotNull
    private TipoVoto tipoVoto;
    
}
