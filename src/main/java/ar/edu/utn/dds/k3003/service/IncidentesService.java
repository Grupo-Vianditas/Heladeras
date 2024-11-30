package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.model.incidente.TipoIncidenteEnum;
import ar.edu.utn.dds.k3003.persistance.mappers.IncidenteMapper;
import ar.edu.utn.dds.k3003.persistance.repos.incidentes.IncidenteRepositoryImpl;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.IncidenteDTO;


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

}