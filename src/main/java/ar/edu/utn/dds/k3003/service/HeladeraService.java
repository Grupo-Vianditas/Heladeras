package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.persistance.repos.heladeras.HeladeraRepositoryImpl;
import ar.edu.utn.dds.k3003.persistance.mappers.HeladeraMapper;

import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.Enums.HabilitacionEnum;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.HabilitacionDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.CreateHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.ReturningHeladeraDTO;


import java.util.NoSuchElementException;

public class HeladeraService {

    private final HeladeraMapper mapper;
    private final HeladeraRepositoryImpl repo;

    public HeladeraService(){
        mapper = new HeladeraMapper();
        repo = new HeladeraRepositoryImpl();
    }

    public void save(Heladera heladera){
        repo.save(heladera);
    }

    public ReturningHeladeraDTO createAndSave(CreateHeladeraDTO heladeraDTO){
        Heladera heladera = mapper.toEntity(heladeraDTO);
        save(heladera);
        return mapper.toEntity(heladera);
    }

    public ReturningHeladeraDTO findDTOById(Integer heladeraId) throws NoSuchElementException{
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
        heladera.habilitar();
        updateHeladera(heladera);
        return new HabilitacionDTO(heladeraId, HabilitacionEnum.HABILITADA);
    }

    // Solo deberías poder deshabilitar heladeras VACIAS.
    public HabilitacionDTO deshabilitar(Integer heladeraId){
        Heladera heladera = findHeladeraById(heladeraId);

        heladera.deshabilitar();
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
