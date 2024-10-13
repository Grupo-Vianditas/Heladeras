package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.model.errors.ErrorTipo;
import ar.edu.utn.dds.k3003.model.errors.OperacionInvalidaException;
import ar.edu.utn.dds.k3003.model.estados.Estado;
import ar.edu.utn.dds.k3003.model.estados.Operaciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "heladeras")
public class Heladera {

    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer heladeraId;

    @NotNull
    @Column(unique=true)
    private String nombre;

    @Transient
    private Estado estadoTransaccional;

    @Min(0)
    @Column(name = "cantidadDeViandasDepositadas")
    private Integer cantidadDeViandas;

    @Column(name = "cantidadDeViandasMaxima")
    private Integer cantidadDeViandasMaxima;

    @Column(columnDefinition = "DATE", name = "FechaDeFuncionamiento")
    private LocalDateTime fechaDeFuncionamiento;

    @Column(name = "estadoOperacional")
    private Boolean estadoOperacional;

    @Column(name = "ultimaTemperaturaRegistrada")
    private Integer ultimaTemperaturaRegistrada;

    @Column(name = "ultimaApertura", columnDefinition = "DATE")
    private LocalDateTime ultimaApertura;

    @Enumerated(EnumType.STRING)
    @Column(name = "ultimaOperacion")
    private Operaciones ultimaOperacion;

    @OneToMany(mappedBy = "heladera", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Temperatura> temperaturas;

    public Heladera() {}

    public Heladera(
            Integer heladeraId,
            String nombre,
            Integer cantidadDeViandas
    ) {
        this.heladeraId = heladeraId;
        this.nombre = nombre;
        this.cantidadDeViandas = (cantidadDeViandas != null) ? cantidadDeViandas : 0;
        this.estadoTransaccional = Estado.CERRADA;
        this.cantidadDeViandasMaxima = 100;
        this.fechaDeFuncionamiento = LocalDateTime.now();
        this.estadoOperacional = true;
        this.ultimaTemperaturaRegistrada = null;
        this.ultimaApertura = null;
        this.ultimaOperacion = Operaciones.SIN_MOVIMIENTOS;
        this.temperaturas = new ArrayList<>();

        if (cantidadDeViandas != null) {
            if (cantidadDeViandas > this.cantidadDeViandasMaxima) {
                throw new OperacionInvalidaException(
                        ErrorTipo.CANTIDAD_EXCESIVA,
                        "La cantidad inicial de viandas no puede superar la capacidad máxima de viandas."
                );
            }
            this.cantidadDeViandas = cantidadDeViandas;
        } else {
            this.cantidadDeViandas = 0;
        }
    }

    public void agregarVianda() {
        if (this.getCantidadDeViandas() + 1 > this.getCantidadDeViandasMaxima()) {
            throw new OperacionInvalidaException(
                    ErrorTipo.CANTIDAD_EXCESIVA,
                    "No se pueden agregar más viandas a esta heladera."
            );
        }

        if (this.getEstadoTransaccional() == Estado.CERRADA) {
            this.setEstadoTransaccional(Estado.ABIERTA);
        }

        this.setCantidadDeViandas(this.getCantidadDeViandas() + 1);

        this.setEstadoTransaccional(Estado.CERRADA);

        this.setUltimaOperacion(Operaciones.DEPOSITO);
        this.setUltimaApertura(LocalDateTime.now());
    }


    public void retirarVianda() throws OperacionInvalidaException {
        if (this.getCantidadDeViandas() < 0) {
            throw new OperacionInvalidaException(ErrorTipo.CANTIDAD_FALTANTE, "No se pueden retirar mas viandas de esta heladera.");
        }

        if (this.getEstadoTransaccional() == Estado.CERRADA) {
            this.setEstadoTransaccional(Estado.ABIERTA);
        }

        this.setCantidadDeViandas(this.getCantidadDeViandas() - 1);

        this.setEstadoTransaccional(Estado.CERRADA);

        this.setUltimaOperacion(Operaciones.RETIRO);
        this.setUltimaApertura(LocalDateTime.now());
    }

    public void agregarTemperatura(Temperatura temperatura){
        temperatura.setHeladera(this);
        this.temperaturas.add(temperatura);
        this.ultimaTemperaturaRegistrada = temperatura.getTemperatura();
    }

}