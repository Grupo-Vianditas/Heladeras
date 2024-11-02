package ar.edu.utn.dds.k3003.model.incidentes.productores;

import ar.edu.utn.dds.k3003.model.incidentes.GeneradorIncidentes;
import ar.edu.utn.dds.k3003.model.incidentes.Incidente;
import ar.edu.utn.dds.k3003.model.incidentes.concretos.IncidenteTecnico;

public class GeneradorIncidentesTecnicos extends GeneradorIncidentes {
    @Override
    public Incidente crearIncidente(Integer heladeraId) {
        return new IncidenteTecnico(heladeraId);
    }
}
