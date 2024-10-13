package ar.edu.utn.dds.k3003.persistance.repos.temperaturas;

import ar.edu.utn.dds.k3003.model.Temperatura;

public interface TemperaturaRepository {

    void save(Temperatura temperatura);
    void clear();
}
