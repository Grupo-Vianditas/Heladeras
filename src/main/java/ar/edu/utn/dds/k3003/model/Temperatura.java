package ar.edu.utn.dds.k3003.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@Entity(name = "Temperatura")
@Table(name = "temperaturas")
public class Temperatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temperaturaId")
    private Integer id;

    @NotNull
    //@Transient
    @Column(name = "heladeraId", insertable = false, updatable = false)
    private Integer heladeraId;

    @NotNull
    @Column
    private Integer temperatura;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
    @Column(name = "fechaMedicion")
    private LocalDateTime fechaMedicion;

    @ManyToOne
    @JoinColumn(name = "heladeraId")
    private Heladera heladera;

    public Temperatura() {}

    public Temperatura(Integer temperatura, Integer heladeraId, LocalDateTime fechaMedicion) {
        this.temperatura = temperatura;
        this.heladeraId = heladeraId;
        this.fechaMedicion = fechaMedicion;
    }

}