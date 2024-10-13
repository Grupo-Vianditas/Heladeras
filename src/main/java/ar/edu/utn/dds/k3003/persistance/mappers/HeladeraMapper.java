package ar.edu.utn.dds.k3003.persistance.mappers;

import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.model.Heladera;

public class HeladeraMapper {

    //Mapper HeladeraDTO -> Heladera
    public Heladera toEntity(HeladeraDTO heladeraDTO){
        return new Heladera(
                heladeraDTO.getId(),
                heladeraDTO.getNombre(),
                heladeraDTO.getCantidadDeViandas()
        );
    }

    //Mapper Heladera -> HeladeraDTO
    public HeladeraDTO toEntity(Heladera heladera){
        return new HeladeraDTO(
                heladera.getHeladeraId(),
                heladera.getNombre(),
                heladera.getCantidadDeViandas()
        );
    }
}
