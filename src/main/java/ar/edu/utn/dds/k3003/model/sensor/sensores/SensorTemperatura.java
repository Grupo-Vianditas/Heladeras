package ar.edu.utn.dds.k3003.model.sensor.sensores;

import ar.edu.utn.dds.k3003.infra.Event;
import ar.edu.utn.dds.k3003.infra.eventTypes.EventType;
import ar.edu.utn.dds.k3003.model.sensor.Sensor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "sensoresdetemperatura")
public class SensorTemperatura extends Sensor{

    @Column
    private Integer temperaturaActual;

    @Column
    private Integer temperaturaMinima;

    @Column
    private Integer temperaturaMaxima;

    @Column
    private Integer tiempoMaximoFueraDeRango;

    public SensorTemperatura() {
        this.temperaturaMinima = -5;
        this.temperaturaMaxima = 10;
        this.tiempoMaximoFueraDeRango = 30;
    }

    public void actualizarTemperatura(Integer temperatura){
        this.temperaturaActual = temperatura;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getTipo() == EventType.CAMBIO_TEMPERATURA) {
            this.temperaturaActual = (Integer) event.getData();
            verificarAlerta();
        }
    }

    @Override
    public void verificarAlerta() {
        LocalDateTime tiempoActual = LocalDateTime.now();
        if ((temperaturaActual < temperaturaMinima || temperaturaActual > temperaturaMaxima) &&
                ultimaLectura.isBefore(tiempoActual.minusSeconds(tiempoMaximoFueraDeRango))) {
            ultimaLectura = tiempoActual;
            generarIncidente(tiempoActual);
        }
    }

    private void generarIncidente(LocalDateTime tiempoActual) {
        System.out.println("Alerta de temperatura : " + tiempoActual);
    }

}

