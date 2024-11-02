package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;

import ar.edu.utn.dds.k3003.model.incidentes.subtipos.SubtipoAlerta;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.HabilitacionDTO;
import ar.edu.utn.dds.k3003.service.HeladeraService;
import ar.edu.utn.dds.k3003.service.ImpresionService;
import ar.edu.utn.dds.k3003.service.IncidentesService;
import ar.edu.utn.dds.k3003.service.TemperaturaService;

import lombok.Getter;

import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaHeladeras{

    private final HeladeraService heladeraService;
    private final TemperaturaService temperaturaService;
    private final IncidentesService incidentesService;
    private final ImpresionService impresionService;

    private FachadaViandas fachadaViandas;

    public Fachada(){
        this.heladeraService = new HeladeraService();
        this.temperaturaService = new TemperaturaService();
        this.incidentesService = new IncidentesService();
        this.impresionService = new ImpresionService();
    }

    @Override
    public HeladeraDTO agregar(HeladeraDTO heladeraDTO) {
       return heladeraService.createAndSave(heladeraDTO);
    }

    public HeladeraDTO buscarXId(Integer heladeraId){
        return heladeraService.findDTOById(heladeraId);
    }

    public HabilitacionDTO habilitar(Integer heladeraId){
        return heladeraService.habilitar(heladeraId);
    }

    public HabilitacionDTO inhabilitar(Integer heladeraId){
        return heladeraService.inhabilitar(heladeraId);
    }

    public HabilitacionDTO generarIncidenteAlerta(Integer heladeraId, SubtipoAlerta subtipoAlerta){
        HabilitacionDTO nuevoEstado = heladeraService.inhabilitar(heladeraId);
        impresionService.imprimirIncidente(incidentesService.generarIncidente("ALERTA", heladeraId, subtipoAlerta));
        return nuevoEstado;
    }

    public HabilitacionDTO generarIncidenteTecnico(Integer heladeraId){
        HabilitacionDTO nuevoEstado = heladeraService.inhabilitar(heladeraId);
        impresionService.imprimirIncidente(incidentesService.generarIncidente("TECNICO", heladeraId, null));
        return nuevoEstado;
    }

    @Override
    public Integer cantidadViandas(Integer heladeraId) throws NoSuchElementException {
        return heladeraService.getCantidadViandas(heladeraId);
    }

    @Override
    public void depositar(Integer heladeraId, String qrVianda) throws NoSuchElementException {
        ViandaDTO vianda = this.fachadaViandas.buscarXQR(qrVianda);
        heladeraService.depositVianda(heladeraId);
        this.fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.DEPOSITADA);
    }

    @Override
    public void retirar(RetiroDTO retiroDTO) throws NoSuchElementException {
        ViandaDTO vianda = this.fachadaViandas.buscarXQR(retiroDTO.getQrVianda());
        heladeraService.withdrawVianda(retiroDTO.getHeladeraId());
        this.fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.RETIRADA);
    }

    @Override
    public void temperatura(TemperaturaDTO temperaturaDTO) {
        temperaturaService.createAndSave(temperaturaDTO);
    }

    @Override
    public List<TemperaturaDTO> obtenerTemperaturas(Integer heladeraId) {
        return temperaturaService.getDTOTemps(
                temperaturaService.getTempsByHeladeraId(heladeraId)
        );
    }

    public void purgarTodo(){
        temperaturaService.clear();
        heladeraService.clear();
    }



    @Override
    public void setViandasProxy(FachadaViandas viandas){
        this.fachadaViandas = viandas;
    }
}
