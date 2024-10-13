package ar.edu.utn.dds.k3003.persistance.repos.heladeras;


import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Temperatura;

public interface HeladeraRepository {

    void save(Heladera heladera);
    void modifyHeladera(Heladera heladera);
    void clear();

    Heladera getHeladeraById(Integer id);

    Integer getCantidadViandas(Integer heladeraId);
}
