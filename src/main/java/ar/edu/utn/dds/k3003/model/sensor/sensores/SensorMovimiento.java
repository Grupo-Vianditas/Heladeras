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
@Table(name = "sensoresmovimiento")
public class SensorMovimiento extends Sensor {

    @Column
    private Boolean activo;

    public SensorMovimiento() {
        this.activo = true;
    }


    @Override
    public void verificarAlerta() {
        if (Math.random() < 0.3) { // 30% de probabilidad de detectar movimiento
            generarIncidente(LocalDateTime.now());
        }
    }

    @Override
    public void onEvent(Event event) {
        if (event.getTipo() == EventType.MOVIMIENTO_DETECTADO) {
           verificarAlerta();
        }
    }

    private void generarIncidente(LocalDateTime tiempoActual) {
        System.out.println("Alerta de movimiento:" + tiempoActual);
    }
}



