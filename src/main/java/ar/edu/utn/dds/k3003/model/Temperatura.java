package ar.edu.utn.dds.k3003.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity(name = "Temperatura")
@Table(name = "temperaturas")
public class Temperatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "temperatura_id")
    private Integer id;

    @NotNull
    @Transient
    private Integer heladeraid;

    @NotNull
    @Column
    private Integer temperatura;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", shape = JsonFormat.Shape.STRING)
    @Column(name = "fecha_medicion")
    private LocalDateTime fechaMedicion;

    @ManyToOne
    @JoinColumn(name = "heladera_id")
    private Heladera heladera;

    public Temperatura() {}

    public Temperatura(Integer heladeraid, Integer temperatura, LocalDateTime fechaMedicion) {
        this.heladeraid = heladeraid;
        this.temperatura = temperatura;
        this.fechaMedicion = fechaMedicion;
        validate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Temperatura)) return false;
        return id != null && id.equals(((Temperatura) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    private void validate() {
        Validator validator;
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
        Set<ConstraintViolation<Temperatura>> violations = validator.validate(this);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            int count = 0;
            for (ConstraintViolation<Temperatura> violation : violations) {
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
