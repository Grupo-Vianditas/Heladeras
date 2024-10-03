package ar.edu.utn.dds.k3003.repositories;

import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.model.Temperatura;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import java.util.List;

public class TemperaturaMapper {

    public TemperaturaDTO map(Temperatura temperatura, Integer heladeraId){
        return new TemperaturaDTO(
                temperatura.getTemperatura(),
                heladeraId,
                temperatura.getFechaMedicion());
    }

    public Temperatura map(TemperaturaDTO temperaturaDTO){

        return new Temperatura(
                temperaturaDTO.getHeladeraId(),
                temperaturaDTO.getTemperatura(),
                temperaturaDTO.getFechaMedicion()
        );
    }

    public List<TemperaturaDTO> convertirATemperaturasDTO(List<Temperatura> temperaturas, Integer heladeraId) {
        return temperaturas.stream()
                .map(temperatura -> map(temperatura, heladeraId))
                .collect(Collectors.toList());
    }

}
