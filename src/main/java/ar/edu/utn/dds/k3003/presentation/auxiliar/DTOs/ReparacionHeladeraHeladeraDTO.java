package ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReparacionHeladeraHeladeraDTO {
    private Long IdHeladera;
    private Long IdColaborador;
    private LocalDateTime fecha;

    public ReparacionHeladeraHeladeraDTO(){};
}