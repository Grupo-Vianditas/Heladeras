package ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs;

import ar.edu.utn.dds.k3003.model.incidente.TipoIncidenteEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IncidenteDTO {
    private Integer heladeraId;
    private Integer colaboradorId;
    private String denunciante;
    private TipoIncidenteEnum tipoIncidente;

    public IncidenteDTO(){}
}
