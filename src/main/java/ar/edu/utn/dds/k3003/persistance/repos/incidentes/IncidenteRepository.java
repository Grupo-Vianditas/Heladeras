package ar.edu.utn.dds.k3003.persistance.repos.incidentes;

import ar.edu.utn.dds.k3003.model.Incidente;

import java.util.List;

public interface IncidenteRepository {

    void save(Incidente incidente);
    public void clear();

    public Incidente getById(Integer incidenteId);

    public List<Incidente> getIncidentesDeHeladera(Integer heladeraId);
}
