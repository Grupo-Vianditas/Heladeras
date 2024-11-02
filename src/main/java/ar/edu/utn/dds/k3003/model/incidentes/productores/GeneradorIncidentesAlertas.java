package ar.edu.utn.dds.k3003.model.incidentes.productores;

import ar.edu.utn.dds.k3003.model.incidentes.GeneradorIncidentes;
import ar.edu.utn.dds.k3003.model.incidentes.Incidente;
import ar.edu.utn.dds.k3003.model.incidentes.concretos.IncidenteAlerta;
import ar.edu.utn.dds.k3003.model.incidentes.subtipos.SubtipoAlerta;


public class GeneradorIncidentesAlertas extends GeneradorIncidentes {
    @Override
    public Incidente crearIncidente(Integer heladeraId) {
        throw new UnsupportedOperationException("Debe especificarse un subtipo de alerta.");
    }

    public Incidente crearIncidente(Integer heladeraId, SubtipoAlerta subtipoAlerta) {
        return new IncidenteAlerta(heladeraId, subtipoAlerta);
    }
}