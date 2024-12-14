package ar.edu.utn.dds.k3003.service;


import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.model.Retiro;
import ar.edu.utn.dds.k3003.persistance.mappers.RetiroMapper;
import ar.edu.utn.dds.k3003.persistance.repos.retiros.RetirosRepositoryImpl;

import java.util.List;

public class RetirosService {

    private final RetirosRepositoryImpl repo;
    private final RetiroMapper mapper;

    public RetirosService(){
        repo = new RetirosRepositoryImpl();
        mapper = new RetiroMapper();
    }

    public void save(Retiro retiro){
        repo.save(retiro);
    }

    public List<RetiroDTO> getDailyRetirosByHeladeraId(Integer heladeraId){
        return mapper.convertRetirosToDTO(repo.getDailyRetirosByHeladeraId(heladeraId));
    }

    public void clear(){
        repo.clear();
    }
}
