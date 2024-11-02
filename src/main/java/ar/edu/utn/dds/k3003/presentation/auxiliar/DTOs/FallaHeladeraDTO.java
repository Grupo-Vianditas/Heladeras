package ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FallaHeladeraDTO {
    private Integer heladeraId;
    private LocalDateTime fecha;
}
