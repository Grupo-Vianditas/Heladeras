package ar.edu.utn.dds.k3003.service;


import ar.edu.utn.dds.k3003.model.Retiro;
import ar.edu.utn.dds.k3003.persistance.repos.retiros.RetirosRepositoryImpl;

public class RetirosService {

    private final RetirosRepositoryImpl repo;

    public RetirosService(){
        repo = new RetirosRepositoryImpl();
    }

    public void save(Retiro retiro){
        repo.save(retiro);
    }

    public Integer getDailyRetirosByHeladeraId(Integer heladeraId){
        return repo.getDailyRetirosByHeladeraId(heladeraId);
    }

    public void clear(){
        repo.clear();
    }
}
