package ar.edu.utn.dds.k3003.persistance.repos.heladeras;


import ar.edu.utn.dds.k3003.model.Heladera;

public interface HeladeraRepository {

    void save(Heladera heladera);
    void modify(Heladera heladera);
    void clear();

    Heladera getById(Integer id);

    Integer getCantidadViandas(Integer heladeraId);
}
