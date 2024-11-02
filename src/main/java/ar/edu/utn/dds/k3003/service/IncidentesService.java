package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.incidentes.Incidente;
import ar.edu.utn.dds.k3003.model.incidentes.productores.GeneradorIncidentesAlertas;
import ar.edu.utn.dds.k3003.model.incidentes.productores.GeneradorIncidentesTecnicos;
import ar.edu.utn.dds.k3003.model.incidentes.subtipos.SubtipoAlerta;

public class IncidentesService {
    private final GeneradorIncidentesAlertas generadorIncidentesAlertas;
    private final GeneradorIncidentesTecnicos generadorIncidentesTecnicos;

    public IncidentesService() {
        this.generadorIncidentesAlertas = new GeneradorIncidentesAlertas();
        this.generadorIncidentesTecnicos = new GeneradorIncidentesTecnicos();
    }

    public Incidente generarIncidente(String tipo, Integer heladeraId, SubtipoAlerta subtipoAlerta) {
        if ("ALERTA".equals(tipo) && subtipoAlerta != null) {
            return generadorIncidentesAlertas.crearIncidente(heladeraId, subtipoAlerta);
        } else if ("TECNICO".equals(tipo)) {
            return generadorIncidentesTecnicos.crearIncidente(heladeraId);
        } else {
            throw new IllegalArgumentException("Tipo de incidente no soportado o subtipo de alerta faltante.");
        }
    }

}
