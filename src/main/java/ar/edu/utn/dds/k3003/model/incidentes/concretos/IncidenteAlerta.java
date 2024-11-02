package ar.edu.utn.dds.k3003.model.incidentes.concretos;

import ar.edu.utn.dds.k3003.model.incidentes.IncidenteBase;
import ar.edu.utn.dds.k3003.model.incidentes.subtipos.SubtipoAlerta;


public class IncidenteAlerta extends IncidenteBase {

    private final SubtipoAlerta subtipoAlerta;

    public IncidenteAlerta(Integer heladeraId, SubtipoAlerta subtipoAlerta) {
        super(heladeraId, "ALERTA");
        this.subtipoAlerta = subtipoAlerta;
    }

    public SubtipoAlerta getSubtipoAlerta() {
        return subtipoAlerta;
    }

    @Override
    public void crear(Integer heladeraId) {
        // Método vacío, opcional según el tipo de incidente
    }
}
