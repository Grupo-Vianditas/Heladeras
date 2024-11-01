package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.model.errors.ErrorTipo;
import ar.edu.utn.dds.k3003.model.errors.OperacionInvalidaException;
import ar.edu.utn.dds.k3003.model.estados.Estado;
import ar.edu.utn.dds.k3003.model.estados.Operaciones;
import ar.edu.utn.dds.k3003.model.sensor.Sensor;
import ar.edu.utn.dds.k3003.model.sensor.sensores.SensorConexion;
import ar.edu.utn.dds.k3003.model.sensor.sensores.SensorMovimiento;
import ar.edu.utn.dds.k3003.model.sensor.sensores.SensorTemperatura;
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

    @Column(name = "estadoApertura")
    private Estado estadoApertura;

    @Min(0)
    @Column(name = "cantidadDeViandasDepositadas")
    private Integer cantidadDeViandas;

    @Column(name = "cantidadDeViandasMaxima")
    private Integer cantidadDeViandasMaxima;

    @Column(name = "fechaDeFuncionamiento", columnDefinition = "TIMESTAMP")
    private LocalDateTime fechaDeFuncionamiento;

    @Column(name = "estadoOperacional")
    private Boolean estadoOperacional;

    @Column(name = "ultimaTemperaturaRegistrada")
    private Integer ultimaTemperaturaRegistrada;

    @Column(name = "umbralDemoraDesconexion")
    private Integer umbralDesconexion;

    @Column(name = "umbralDemoraTemperatura")
    private Integer umbralTemperatura;

    @Column(name = "ultimaApertura", columnDefinition = "TIMESTAMP")
    private LocalDateTime ultimaApertura;

    @Enumerated(EnumType.STRING)
    @Column(name = "ultimaOperacion")
    private Operaciones ultimaOperacion;

    @OneToMany(mappedBy = "heladera", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Temperatura> temperaturas;

    @OneToMany(mappedBy = "heladera", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Sensor> sensores;

    public Heladera() {}

    public Heladera(
            Integer heladeraId,
            String nombre,
            Integer cantidadDeViandas
    ) {
        this.heladeraId = heladeraId;
        this.nombre = nombre;
        this.cantidadDeViandas = (cantidadDeViandas != null) ? cantidadDeViandas : 0;
        this.estadoApertura = Estado.CERRADA;
        this.cantidadDeViandasMaxima = 10;
        this.fechaDeFuncionamiento = LocalDateTime.now();
        this.estadoOperacional = true;
        this.ultimaApertura = null;
        this.ultimaOperacion = Operaciones.SIN_MOVIMIENTOS;
        this.temperaturas = new ArrayList<>();
        this.sensores = new ArrayList<>();

        inicializarSensores();

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

    private void inicializarSensores() {
        this.sensores.add(new SensorMovimiento());
        this.sensores.add(new SensorConexion());
        this.sensores.add(new SensorTemperatura());
    }

    public void agregarVianda() {
        if (!this.estadoOperacional){
            throw new OperacionInvalidaException(
                    ErrorTipo.HELADERA_INACTIVA,
                    "La heladera se encuentra inactiva."
            );
        }

        if (this.getCantidadDeViandas() + 1 > this.getCantidadDeViandasMaxima()) {
            throw new OperacionInvalidaException(
                    ErrorTipo.CANTIDAD_EXCESIVA,
                    "No se pueden agregar más viandas a esta heladera."
            );
        }

        this.sensores.stream()
                .filter(sensor -> sensor instanceof SensorMovimiento)
                .map(sensor -> (SensorMovimiento) sensor)
                .findFirst()
                .ifPresent(SensorMovimiento::verificarAlerta);

        if (this.getEstadoApertura() == Estado.CERRADA) {
            this.setEstadoApertura(Estado.ABIERTA);
        }

        this.setCantidadDeViandas(this.getCantidadDeViandas() + 1);

        this.setEstadoApertura(Estado.CERRADA);

        this.setUltimaOperacion(Operaciones.DEPOSITO);
        this.setUltimaApertura(LocalDateTime.now());
    }

    public void retirarVianda() throws OperacionInvalidaException {
        if (!this.estadoOperacional){
            throw new OperacionInvalidaException(
                    ErrorTipo.HELADERA_INACTIVA,
                    "La heladera se encuentra inactiva."
            );
        }

        if (this.getCantidadDeViandas() < 0) {
            throw new OperacionInvalidaException(ErrorTipo.CANTIDAD_FALTANTE, "No se pueden retirar mas viandas de esta heladera.");
        }

        this.sensores.stream()
                .filter(sensor -> sensor instanceof SensorMovimiento)
                .map(sensor -> (SensorMovimiento) sensor)
                .findFirst()
                .ifPresent(SensorMovimiento::verificarAlerta);

        if (this.getEstadoApertura() == Estado.CERRADA) {
            this.setEstadoApertura(Estado.ABIERTA);
        }

        this.setCantidadDeViandas(this.getCantidadDeViandas() - 1);

        this.setEstadoApertura(Estado.CERRADA);

        this.setUltimaOperacion(Operaciones.RETIRO);
        this.setUltimaApertura(LocalDateTime.now());
    }

    public void agregarTemperatura(Temperatura temperatura){
        temperatura.setHeladera(this);
        this.temperaturas.add(temperatura);
        this.ultimaTemperaturaRegistrada = temperatura.getTemperatura();

        this.sensores.stream()
                .filter(sensor -> sensor instanceof SensorTemperatura)
                .map(sensor -> (SensorTemperatura) sensor)
                .findFirst()
                .ifPresent(sensorTemperatura -> sensorTemperatura.actualizarTemperatura(temperatura.getTemperatura()));

        this.sensores.stream()
                .filter(sensor -> sensor instanceof SensorConexion)
                .map(sensor -> (SensorConexion) sensor)
                .findFirst()
                .ifPresent(sensorConexion -> sensorConexion.actualizarUltimaLectura(LocalDateTime.now()));
    }

    public void marcarInactiva(){
        if (estadoOperacional){
            this.estadoOperacional = false;
            this.ultimaOperacion = Operaciones.DESACTIVAR;
        } else {
            throw new IllegalStateException("La heladera ya se encuentra inactiva");
        }
        // Agregar logs
    }

    public void marcarActiva(){
        if (!estadoOperacional){
            this.estadoOperacional = true;
            this.ultimaOperacion = Operaciones.ACTIVAR;
        } else {
            throw new IllegalStateException("La heladera ya se encuentra activa");
        }
        // Agregar logs
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