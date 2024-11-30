package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.model.errors.ErrorTipo;
import ar.edu.utn.dds.k3003.model.errors.OperacionInvalidaException;

import ar.edu.utn.dds.k3003.model.estados.Operaciones;

import ar.edu.utn.dds.k3003.model.heladera.HabilitacionEnum;
import ar.edu.utn.dds.k3003.model.incidente.TipoIncidenteEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;


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

    @Min(0)
    @Column(name = "cantidadDeViandasDepositadas")
    private Integer cantidadDeViandas;

    @Column(name = "cantidadDeViandasMaxima")
    private Integer cantidadDeViandasMaxima;

    @Column(name = "fechaDeFuncionamiento", columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaDeFuncionamiento;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoOperacional")
    private HabilitacionEnum habilitacion;

    @Column(name = "ultimaTemperaturaRegistrada")
    private Integer ultimaTemperaturaRegistrada;

    @Column(name = "minimoTemperatura")
    private Integer minimoTemperatura;

    @Column(name = "maximoTemperatura")
    private Integer maximoTemperatura;

    @Column(name = "ultimaApertura", columnDefinition = "TIMESTAMP")
    private LocalDateTime ultimaApertura;

    @Enumerated(EnumType.STRING)
    @Column(name = "ultimaOperacion")
    private Operaciones ultimaOperacion;

    @OneToMany(mappedBy = "heladera", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Temperatura> temperaturas;

    @OneToMany(mappedBy = "heladera", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    private Set<Incidente> incidentes;

    public Heladera() {}

    public Heladera(
            String nombre,
            Integer cantidadDeViandas,
            HabilitacionEnum habilitacion
    ) {
        this.nombre = nombre;
        this.cantidadDeViandas = cantidadDeViandas;
        this.cantidadDeViandasMaxima = 10;
        this.minimoTemperatura = -5; // Hardcodeado
        this.maximoTemperatura = 15; // Hardcodeado
        this.fechaDeFuncionamiento = LocalDateTime.now();
        this.habilitacion = habilitacion;
        this.ultimaApertura = null;
        this.ultimaOperacion = Operaciones.SIN_MOVIMIENTOS;
        this.temperaturas = new HashSet<>();
        this.incidentes = new HashSet<>();

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

    public boolean estaHabilitada(){
        return this.habilitacion == HabilitacionEnum.HABILITADA;
    }

    public void agregarVianda() {
        if (!estaHabilitada()){
            throw new OperacionInvalidaException(
                    ErrorTipo.HELADERA_INACTIVA,
                    "La heladera se encuentra inactiva."
            );
        }

        if (getCantidadDeViandas() + 1 > getCantidadDeViandasMaxima()) {
            throw new OperacionInvalidaException(
                    ErrorTipo.CANTIDAD_EXCESIVA,
                    "No se pueden agregar más viandas a esta heladera."
            );
        }

        setCantidadDeViandas(this.cantidadDeViandas + 1);
        setUltimaOperacion(Operaciones.DEPOSITO);
        setUltimaApertura(LocalDateTime.now());
    }

    public void retirarVianda() throws OperacionInvalidaException {
        if (!estaHabilitada()){
            throw new OperacionInvalidaException(
                    ErrorTipo.HELADERA_INACTIVA,
                    "La heladera se encuentra inactiva."
            );
        } else if (this.getCantidadDeViandas() < 0) {
            throw new OperacionInvalidaException(ErrorTipo.CANTIDAD_FALTANTE, "No se pueden retirar mas viandas de esta heladera.");

        } else {
            this.setCantidadDeViandas(this.getCantidadDeViandas() - 1);
            this.setUltimaOperacion(Operaciones.RETIRO);
            this.setUltimaApertura(LocalDateTime.now());
        }
    }

    public void agregarTemperatura(Temperatura temperatura){
        if (!estaHabilitada()){
            throw new OperacionInvalidaException(
                    ErrorTipo.HELADERA_INACTIVA,
                    "La heladera se encuentra inactiva."
            );
        }else{
            temperatura.setHeladera(this);
            this.temperaturas.add(temperatura);
            this.ultimaTemperaturaRegistrada = temperatura.getTemperatura();
        }
    }

    public void agregarIncidente(Incidente incidente){
        incidente.setHeladera(this);
        this.incidentes.add(incidente);
    }

    public void deshabilitar(){
        if (estaHabilitada()){
            this.habilitacion = HabilitacionEnum.DESHABILITADA;
            this.ultimaOperacion = Operaciones.DESHABILITAR;
        } else {
            throw new IllegalStateException("La heladera ya se encuentra deshabilitada");
        }
    }

    public void habilitar(){
        if (!estaHabilitada()){
            this.habilitacion = HabilitacionEnum.HABILITADA;
            this.ultimaOperacion = Operaciones.HABILITAR;
        } else {
            throw new IllegalStateException("La heladera ya se encuentra habilitada");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Heladera heladera = (Heladera) o;
        return Objects.equals(heladeraId, heladera.heladeraId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heladeraId);
    }


}