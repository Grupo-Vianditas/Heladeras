package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.model.Temperatura;
import ar.edu.utn.dds.k3003.model.incidente.TipoIncidenteEnum;
import ar.edu.utn.dds.k3003.persistance.mappers.IncidenteMapper;
import ar.edu.utn.dds.k3003.persistance.repos.incidentes.IncidenteRepositoryImpl;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.IncidenteDTO;

import java.util.List;


public class IncidentesService {

    private final IncidenteRepositoryImpl repo;
    private final IncidenteMapper mapper;

    public IncidentesService(){
        this.repo = new IncidenteRepositoryImpl();
        this.mapper = new IncidenteMapper();
    }

    public Incidente generarIncidente(IncidenteDTO reporte){
        return mapper.toEntity(reporte);
    }

    public void save(Incidente incidente){
        repo.save(incidente);
    }

    public void clear(){
        repo.clear();
    }

    public List<Incidente> getIncidenteByHeladeraId(Integer heladeraId){
        return repo.getIncidentesDeHeladera(heladeraId);
    }

    public List<IncidenteDTO> getIncidentesDTO(List<Incidente> incidentes){
        return mapper.convertIncidentesToDTO(incidentes);
    }

}