package ar.edu.utn.dds.k3003.persistance.mappers;

import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.model.Retiro;

import java.util.List;
import java.util.stream.Collectors;

public class RetiroMapper {
    public RetiroDTO toEntity(Retiro retiro){
        return new RetiroDTO(retiro.getQrVianda(), retiro.getTarjeta(), retiro.getFechaRetiro(), retiro.getHeladeraId());
    }

    public List<RetiroDTO> convertRetirosToDTO(List<Retiro> retiros) {
        return retiros.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
