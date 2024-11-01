package ar.edu.utn.dds.k3003.model.sensor;

import ar.edu.utn.dds.k3003.infra.Event;
import ar.edu.utn.dds.k3003.infra.EventListener;
import ar.edu.utn.dds.k3003.model.Heladera;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Sensor implements EventListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer sensorId;

    @Column(name = "fechaDeCreacion", columnDefinition = "TIMESTAMP")
    public LocalDateTime fechaDeCreacion;

    @Column(name = "ultimaLetura", columnDefinition = "TIMESTAMP")
    public LocalDateTime ultimaLectura;

    @ManyToOne
    @JoinColumn(name = "heladeraId")
    private Heladera heladera;

    public Sensor() {
        this.fechaDeCreacion = LocalDateTime.now();
    }

    public abstract void verificarAlerta();

    public abstract void onEvent(Event event);

    public void actualizarUltimaLectura(LocalDateTime ultimaLectura){
        this.ultimaLectura = ultimaLectura;
    }
}
