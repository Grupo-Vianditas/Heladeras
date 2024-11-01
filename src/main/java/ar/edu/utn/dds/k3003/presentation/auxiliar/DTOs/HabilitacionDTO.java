package ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs;

import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.Enums.HabilitacionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class HabilitacionDTO {
    private Integer heladeraId;
    private HabilitacionEnum habilitacion;
}
