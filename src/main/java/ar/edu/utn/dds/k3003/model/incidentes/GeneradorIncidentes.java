package ar.edu.utn.dds.k3003.model.incidentes;

public abstract class GeneradorIncidentes {

    public Incidente generarIncidente(Integer heladeraId) {
        return crearIncidente(heladeraId);
    }

    protected abstract Incidente crearIncidente(Integer heladeraId);
}
