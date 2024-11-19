package ar.edu.utn.dds.k3003.persistance.mappers;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.CreateHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.ReturningHeladeraDTO;

public class HeladeraMapper {

    //Mapper NewHeladeraDTO -> Heladera
    public Heladera toEntity(CreateHeladeraDTO heladeraDTO){
        return new Heladera(
                heladeraDTO.getNombre(),
                heladeraDTO.getCantidadDeViandas(),
                heladeraDTO.getHabilitacion()
        );
    }

    //Mapper Heladera -> NewHeladeraDTO
    public ReturningHeladeraDTO toEntity(Heladera heladera){
        return new ReturningHeladeraDTO(
                heladera.getHeladeraId(),
                heladera.getNombre(),
                heladera.getCantidadDeViandas(),
                heladera.getHabilitacion()
        );
    }
}
