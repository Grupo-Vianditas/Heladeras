package ar.edu.utn.dds.k3003.model.incidente;

import ar.edu.utn.dds.k3003.infra.eventTypes.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Incidente {

    private Integer heladeraId;
    private TipoIncidente tipoIncidente;
    private EventType tipoEvento;
    private LocalDateTime fechaHora;

    public Incidente(){}

    public Incidente crearAlertaFallaTecnica(Integer heladeraId){
        return new Incidente(heladeraId, TipoIncidente.FALLA_TECNICA, EventType.VERIFICAR_CONEXION, LocalDateTime.now());
    }

    public Incidente crearAlertaFraude(Integer heladeraId){
        return new Incidente(heladeraId, TipoIncidente.ALERTA, EventType.MOVIMIENTO_DETECTADO, LocalDateTime.now());
    }

    public Incidente crearAlertaConexion(Integer heladeraId){
        return new Incidente(heladeraId, TipoIncidente.ALERTA, EventType.VERIFICAR_CONEXION, LocalDateTime.now());
    }

    public Incidente crearAlertaTemperatura(Integer heladeraId){
        return new Incidente(heladeraId, TipoIncidente.ALERTA, EventType.CAMBIO_TEMPERATURA, LocalDateTime.now());
    }


}
