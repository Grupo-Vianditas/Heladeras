package ar.edu.utn.dds.k3003.persistance.mappers;

import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.IncidenteDTO;

public class IncidenteMapper {

    public Incidente toEntity(IncidenteDTO reporte){
        return new Incidente(
                reporte.getHeladeraId(),
                reporte.getColaboradorId(),
                reporte.getDenunciante(),
                reporte.getTipoIncidente()
        );
    }
}
