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
@Table(name = "sensoresconexion")
public class SensorConexion extends Sensor {

    @Column
    private Integer tiempoMaximoSinConexion;

    public SensorConexion() {
        this.tiempoMaximoSinConexion = 30;
    }

    @Override
    public void onEvent(Event event) {
        if (event.getTipo() == EventType.CAMBIO_TEMPERATURA) {
            ultimaLectura = LocalDateTime.now();
        } else if (event.getTipo() == EventType.VERIFICAR_CONEXION) {
            verificarAlerta();
        }
    }

    @Override
    public void verificarAlerta() {
        LocalDateTime tiempoActual = LocalDateTime.now();
        if (ultimaLectura.isBefore(tiempoActual.minusSeconds(tiempoMaximoSinConexion))) {
            generarIncidente(tiempoActual);
        }
    }

    private void generarIncidente(LocalDateTime tiempoActual) {
        System.out.println("Alerta de desconexión: No se ha recibido lectura de temperatura desde " + ultimaLectura);
    }
}

