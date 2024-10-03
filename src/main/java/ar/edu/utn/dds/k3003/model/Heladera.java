package ar.edu.utn.dds.k3003.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "heladeras")
public class Heladera{

    @Min(0)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @NotNull
    @Column(unique=true)
    private String nombre;

    @Min(0)
    @Column(name = "cantidad_de_viandas")
    private Integer cantidadDeViandas;

    @Column(columnDefinition = "DATE", name = "fecha_de_funcionamiento")
    private LocalDateTime fechaDeFuncionamiento;

    @Column(name = "estado_operacional")
    private Boolean estadoOperacional;

    @Column(name = "ultima_temperatura_registrada")
    private Integer ultimaTemperaturaRegistrada;

    @Column(name = "ultima_apertura", columnDefinition = "DATE")
    private LocalDateTime ultimaApertura;

    @Enumerated(EnumType.STRING)
    @Column(name = "ultimo_movimiento")
    private Movimientos ultimoMovimiento;

    @OneToMany(mappedBy = "heladera", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Temperatura> temperaturas;

    public Heladera() {}

    public Heladera(
            Integer id,
            String nombre,
            Integer cantidadDeViandas
    ) {
        if (cantidadDeViandas == null) {
            cantidadDeViandas = 0;
        }

        this.id = id;
        this.nombre = nombre;
        this.cantidadDeViandas = cantidadDeViandas;
        this.fechaDeFuncionamiento = LocalDateTime.now();
        this.estadoOperacional = true;
        this.ultimaTemperaturaRegistrada = null;
        this.ultimaApertura = null;
        this.ultimoMovimiento = Movimientos.SIN_MOVIMIENTOS;
        this.temperaturas = new ArrayList<>();
        validate();
    }

    public void agregarTemperatura(Temperatura temperatura) {
        this.temperaturas.add(0, temperatura);
        temperatura.setHeladera(this); // Establecer la relación bidireccional
    }

    public void setCantidadDeViandas(Integer cantidadDeViandas) {
        if (cantidadDeViandas < 0) {
            throw new IllegalArgumentException("La cantidad de viandas no puede ser negativa.");
        }
        if (this.cantidadDeViandas == 0 && cantidadDeViandas < this.cantidadDeViandas) {
            throw new IllegalArgumentException("No se puede decrementar la cantidad de viandas cuando ya es 0.");
        }
        this.cantidadDeViandas = cantidadDeViandas;
    }

    private void validate() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<Heladera>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (ConstraintViolation<Heladera> violation : violations) {
                sb.append(violation.getPropertyPath())
                        .append(" : ")
                        .append(violation.getMessage());
                count++;
                if (count < violations.size()) {
                    sb.append(" & ");
                }
            }
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
