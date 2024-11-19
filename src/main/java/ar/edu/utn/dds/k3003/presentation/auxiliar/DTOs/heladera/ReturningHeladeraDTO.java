package ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera;

import ar.edu.utn.dds.k3003.model.heladera.HabilitacionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReturningHeladeraDTO {
    Integer heladeraId;
    String nombre;
    Integer cantidadDeViandas;
    HabilitacionEnum habilitacion;

    public ReturningHeladeraDTO(){}
}
