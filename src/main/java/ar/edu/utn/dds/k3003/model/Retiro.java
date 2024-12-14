package ar.edu.utn.dds.k3003.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "retiros")
public class Retiro {

    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer retiroRegistradoId;

    @Column
    private Long id;

    @Column
    private String qrVianda;

    @Column
    private String tarjeta;

    @Column(name = "fechaRetiro", columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaRetiro;

    @Column
    private Integer heladeraId;

    public Retiro() {}

    public Retiro(Long id, String qrVianda, String tarjeta, LocalDateTime fechaRetiro, Integer heladeraId) {
        this.id = id;
        this.qrVianda = qrVianda;
        this.tarjeta = tarjeta;
        this.fechaRetiro = fechaRetiro;
        this.heladeraId = heladeraId;
    }
}