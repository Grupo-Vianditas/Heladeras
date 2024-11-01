package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.persistance.repos.heladeras.HeladeraRepositoryImpl;
import ar.edu.utn.dds.k3003.persistance.mappers.HeladeraMapper;

import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.Enums.HabilitacionEnum;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.HabilitacionDTO;


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
        return mapper.toEntity(repo.getById(heladeraId));
    }

    public Heladera findHeladeraById(Integer heladeraId) throws NoSuchElementException{
        return repo.getById(heladeraId);
    }

    public void updateHeladera(Heladera heladera){
        repo.modify(heladera);
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

    public HabilitacionDTO habilitar(Integer heladeraId){
        Heladera heladera = findHeladeraById(heladeraId);
        heladera.marcarActiva();
        updateHeladera(heladera);
        return new HabilitacionDTO(heladeraId, HabilitacionEnum.HABILITADA);
    }

    public HabilitacionDTO inhabilitar(Integer heladeraId){
        Heladera heladera = findHeladeraById(heladeraId);
        heladera.marcarInactiva();
        updateHeladera(heladera);
        return new HabilitacionDTO(heladeraId, HabilitacionEnum.INHABILITADA);
    }

    public Integer getCantidadViandas(Integer heladeraId){
        return repo.getCantidadViandas(heladeraId);
    }

    public void clear(){
        repo.clear();
    }

}
