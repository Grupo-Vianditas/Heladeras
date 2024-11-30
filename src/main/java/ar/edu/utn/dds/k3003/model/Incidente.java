package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.model.incidente.TipoIncidenteEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "incidentes")
public class Incidente {

    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer incidenteId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoIncidente")
    private TipoIncidenteEnum tipoIncidente;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "colaboradorId")
    private Integer colaboradorId;

    @Column(name = "denunciante")
    private String denunciante;

    @Column(name = "heladeraId", insertable = false, updatable = false)
    private Integer heladeraId;

    @ManyToOne
    @JoinColumn(name = "heladeraId")
    private Heladera heladera;

    public Incidente(){}

    public Incidente(Integer heladeraId, Integer colaboradorId, String denunciante, TipoIncidenteEnum tipoIncidente){
        this.tipoIncidente = tipoIncidente;
        this.colaboradorId = colaboradorId;
        this.timestamp = LocalDateTime.now();
        this.denunciante = denunciante;
        this.heladeraId = heladeraId;
    }
}
