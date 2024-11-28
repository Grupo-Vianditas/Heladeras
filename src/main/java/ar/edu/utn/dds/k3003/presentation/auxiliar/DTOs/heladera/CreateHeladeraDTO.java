package ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera;

import ar.edu.utn.dds.k3003.model.heladera.HabilitacionEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateHeladeraDTO {
    String nombre;
    Integer cantidadDeViandas;
    HabilitacionEnum habilitacion;

    public CreateHeladeraDTO(){}

    public CreateHeladeraDTO(String nombre) {
        this.nombre = nombre;
        this.cantidadDeViandas = 0;
        this.habilitacion = HabilitacionEnum.HABILITADA;
    }
}
