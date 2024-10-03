package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Movimientos;
import ar.edu.utn.dds.k3003.persistance.HeladerasRepositoryImpl;
import ar.edu.utn.dds.k3003.repositories.HeladeraRepository;
import ar.edu.utn.dds.k3003.repositories.HeladeraMapper;
import ar.edu.utn.dds.k3003.repositories.TemperaturaMapper;

import lombok.Getter;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaHeladeras{

    private final HeladeraMapper heladeraMapper;
    private final HeladeraRepository heladeraRepository;

    private final TemperaturaMapper temperaturaMapper;

    private final HeladerasRepositoryImpl heladeraRepositoryImpl;

    private FachadaViandas fachadaViandas;

    public Fachada(){
        this.heladeraRepository = new HeladeraRepository();
        this.heladeraMapper = new HeladeraMapper();
        this.temperaturaMapper = new TemperaturaMapper();
        this.heladeraRepositoryImpl = new HeladerasRepositoryImpl();
    }

    @Override
    public HeladeraDTO agregar(HeladeraDTO heladeraDTO) {
        return heladeraMapper.map( // 3° Mapea la heladera a DTO
                this.heladeraRepositoryImpl.save( // Guardo la heladera en la bd
                        heladeraMapper.map(heladeraDTO)
                )
        );
    }

    public HeladeraDTO buscarXId(Integer heladeraId) throws NoSuchElementException{
        return this.heladeraMapper.map(
                this.heladeraRepositoryImpl.getHeladeraById(heladeraId)
        );
    }

    public HeladeraDTO buscarXNombre(String nombreHeladera) throws NoSuchElementException{
        return this.heladeraMapper.map(
                this.heladeraRepositoryImpl.getHeladeraByNombre(nombreHeladera)
        );
    }

    @Override
    public void depositar(Integer heladeraId, String qrVianda) throws NoSuchElementException {
        Heladera heladera = this.heladeraRepositoryImpl.getHeladeraById(heladeraId);
        ViandaDTO viandaDTO = this.fachadaViandas.buscarXQR(qrVianda);
        heladera.setCantidadDeViandas(heladera.getCantidadDeViandas()+1);
        heladera.setUltimaApertura(LocalDateTime.now());
        heladera.setUltimoMovimiento(Movimientos.DEPOSITO);
        this.heladeraRepositoryImpl.modifyHeladera(heladera);
        this.fachadaViandas.modificarEstado(viandaDTO.getCodigoQR(), EstadoViandaEnum.DEPOSITADA);
    }

    @Override
    public Integer cantidadViandas(Integer heladeraId) throws NoSuchElementException {
        return this.heladeraRepositoryImpl.getHeladeraById(heladeraId).getCantidadDeViandas();
    }

    @Override
    public void retirar(RetiroDTO retiro) throws NoSuchElementException {
        Heladera heladera = this.heladeraRepositoryImpl.getHeladeraById(retiro.getHeladeraId());
        ViandaDTO viandaDTO = this.fachadaViandas.buscarXQR(retiro.getQrVianda());
        heladera.setCantidadDeViandas(heladera.getCantidadDeViandas()-1);
        heladera.setUltimaApertura(LocalDateTime.now());
        heladera.setUltimoMovimiento(Movimientos.RETIRO);
        this.heladeraRepositoryImpl.modifyHeladera(heladera);
        this.fachadaViandas.modificarEstado(viandaDTO.getCodigoQR(), EstadoViandaEnum.RETIRADA);
    }

    @Override
    public void temperatura(TemperaturaDTO temperatura) {
        this.heladeraRepositoryImpl.agregarTemperatura(this.temperaturaMapper.map(temperatura));
    }


    @Override
    public List<TemperaturaDTO> obtenerTemperaturas(Integer heladeraId) {
        return this.temperaturaMapper.convertirATemperaturasDTO(
                this.heladeraRepositoryImpl.getHeladeraById(heladeraId).getTemperaturas(), heladeraId
        );
    }

    public void purgarTodo(){
        this.heladeraRepositoryImpl.clear();
    }

    @Override
    public void setViandasProxy(FachadaViandas viandas) {
        this.fachadaViandas = viandas;
    }
}
