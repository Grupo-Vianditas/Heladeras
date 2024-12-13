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
    private Integer retiroId;

    @Column
    private Integer heladeraId;

    @Column
    private String qr;

    @Column(name = "fecha", columnDefinition = "TIMESTAMP")
    private LocalDateTime fecha;

    public Retiro() {}

    public Retiro(Integer heladeraId, String qr, LocalDateTime fecha){
        this.heladeraId = heladeraId;
        this.qr = qr;
        this.fecha = fecha;
    }

}
