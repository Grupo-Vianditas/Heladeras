package ar.edu.utn.dds.k3003.model.incidentes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public abstract class IncidenteBase implements Incidente {
    protected LocalDateTime fechaIncidente;
    protected Integer heladeraId;
    protected String tipoIncidente;

    protected IncidenteBase(Integer heladeraId, String tipoIncidente) {
        this.fechaIncidente = LocalDateTime.now();
        this.heladeraId = heladeraId;
        this.tipoIncidente = tipoIncidente;
    }
}
