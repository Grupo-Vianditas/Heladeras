package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Temperatura;
import ar.edu.utn.dds.k3003.persistance.repos.temperaturas.TemperaturaRepositoryImpl;
import ar.edu.utn.dds.k3003.persistance.mappers.TemperaturaMapper;

import java.util.List;

public class TemperaturaService {
    private final TemperaturaMapper mapper;
    private final TemperaturaRepositoryImpl repo;

    public TemperaturaService(){
        mapper = new TemperaturaMapper();
        repo = new TemperaturaRepositoryImpl();
    }

    public Temperatura create(TemperaturaDTO temperaturaDTO){
        return mapper.toEntity(temperaturaDTO);
    }

    public void save(Temperatura temperatura){
        repo.save(temperatura);
    }

    public void createAndSave(TemperaturaDTO temperaturaDTO){
        Temperatura temperatura = create(temperaturaDTO);
        save(temperatura);
    }

    public List<Temperatura> getTempsByHeladeraId(Integer heladeraId){
        return repo.getTemperaturasDeHeladera(heladeraId);
    }

    public List<TemperaturaDTO> getDTOTemps(List<Temperatura> temperaturas){
        return mapper.convertTempsToDTO(temperaturas);
    }

    public void clear(){
        repo.clear();
    }

}
