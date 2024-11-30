package ar.edu.utn.dds.k3003.persistance.mappers;

import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.model.Temperatura;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.IncidenteDTO;

import java.util.List;
import java.util.stream.Collectors;

public class IncidenteMapper {

    public Incidente toEntity(IncidenteDTO reporte){
        return new Incidente(
                reporte.getHeladeraId(),
                reporte.getColaboradorId(),
                reporte.getDenunciante(),
                reporte.getTipoIncidente(),
                reporte.getTimestamp()
        );
    }

    public IncidenteDTO toEntity(Incidente incidente){
        return new IncidenteDTO(
                incidente.getHeladeraId(),
                incidente.getColaboradorId(),
                incidente.getDenunciante(),
                incidente.getTipoIncidente(),
                incidente.getTimestamp()
        );
    }

    public List<IncidenteDTO> convertIncidentesToDTO(List<Incidente> incidentes) {
        return incidentes.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
