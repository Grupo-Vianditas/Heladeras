package ar.edu.utn.dds.k3003.model.incidentes.concretos;

import ar.edu.utn.dds.k3003.model.incidentes.IncidenteBase;

import java.time.LocalDateTime;

public class IncidenteTecnico extends IncidenteBase {

    public IncidenteTecnico(Integer heladeraId) {
        super(heladeraId, "TECNICO");
    }

    @Override
    public void crear(Integer heladeraId) {
        // Método vacío, opcional según el tipo de incidente
    }
}