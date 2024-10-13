package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.errors.OperacionInvalidaException;
import ar.edu.utn.dds.k3003.persistance.repos.heladeras.HeladeraRepositoryImpl;
import ar.edu.utn.dds.k3003.persistance.mappers.HeladeraMapper;

import java.util.NoSuchElementException;

public class HeladeraService {

    private final HeladeraMapper mapper;
    private final HeladeraRepositoryImpl repo;

    public HeladeraService(){
        mapper = new HeladeraMapper();
        repo = new HeladeraRepositoryImpl();
    }

    public Heladera create(HeladeraDTO heladeraDTO){
        return mapper.toEntity(heladeraDTO);
    }

    public HeladeraDTO createDTO(Heladera heladera){
        return mapper.toEntity(heladera);
    }

    public void save(Heladera heladera){
        repo.save(heladera);
    }

    public HeladeraDTO createAndSave(HeladeraDTO heladeraDTO){
        Heladera heladera = create(heladeraDTO);
        save(heladera);
        return createDTO(heladera);
    }

    public HeladeraDTO findDTOById(Integer heladeraId) throws NoSuchElementException{
        return mapper.toEntity(repo.getHeladeraById(heladeraId));
    }

    public Heladera findHeladeraById(Integer heladeraId) throws NoSuchElementException{
        return repo.getHeladeraById(heladeraId);
    }

    public void updateHeladera(Heladera heladera){
        repo.modifyHeladera(heladera);
    }

    public void depositVianda(Integer heladeraId) throws NoSuchElementException{
        Heladera heladera = findHeladeraById(heladeraId);
        heladera.agregarVianda();
        updateHeladera(heladera);
    }

    public void withdrawVianda(Integer heladeraId) throws NoSuchElementException{
        Heladera heladera = findHeladeraById(heladeraId);
        heladera.retirarVianda();
        updateHeladera(heladera);
    }

    public Integer getCantidadViandas(Integer heladeraId){
        return repo.getCantidadViandas(heladeraId);
    }

    public void clear(){
        repo.clear();
    }

}
