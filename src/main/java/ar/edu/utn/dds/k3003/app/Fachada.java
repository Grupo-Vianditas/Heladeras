package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.incidentes.subtipos.SubtipoAlerta;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.FallaHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.HabilitacionDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.MovimientoDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.CreateHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.ReturningHeladeraDTO;
import ar.edu.utn.dds.k3003.service.*;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaHeladeras{

    private final HeladeraService heladeraService;
    private final TemperaturaService temperaturaService;
    private final IncidentesService incidentesService;
    private final ImpresionService impresionService;
    private final NotificadorService notificadorService;

    private FachadaViandas fachadaViandas;

    public Fachada(){
        this.heladeraService = new HeladeraService();
        this.temperaturaService = new TemperaturaService();
        this.incidentesService = new IncidentesService();
        this.impresionService = new ImpresionService();
        this.notificadorService = new NotificadorService();
    }

    //Deprecado porque ahora devuelve un NewHeladeraDTO
    @Override
    public HeladeraDTO agregar(HeladeraDTO heladeraDTO) {
       return heladeraDTO;
    }

    // Nuevo metodo
    public ReturningHeladeraDTO agregar(CreateHeladeraDTO heladeraDTO){
        return heladeraService.createAndSave(heladeraDTO);
    }

    // Nuevo metodo
    public ReturningHeladeraDTO buscarXId(Integer heladeraId){
        return heladeraService.findDTOById(heladeraId);
    }

    // Nuevo metodo
    public HabilitacionDTO habilitar(Integer heladeraId){
        return heladeraService.habilitar(heladeraId);
    }

    public HabilitacionDTO inhabilitar(Integer heladeraId){
        this.notificadorService.enviarFalla(new FallaHeladeraDTO(heladeraId, LocalDateTime.now()));
        return heladeraService.deshabilitar(heladeraId);
    }

    public HabilitacionDTO generarIncidenteAlerta(Integer heladeraId, SubtipoAlerta subtipoAlerta){
        HabilitacionDTO nuevoEstado = heladeraService.deshabilitar(heladeraId);
        impresionService.imprimirIncidente(incidentesService.generarIncidente("ALERTA", heladeraId, subtipoAlerta));
        return nuevoEstado;
    }

    public HabilitacionDTO generarIncidenteTecnico(Integer heladeraId){
        HabilitacionDTO nuevoEstado = heladeraService.deshabilitar(heladeraId);
        impresionService.imprimirIncidente(incidentesService.generarIncidente("TECNICO", heladeraId, null));

        this.notificadorService.enviarFalla(new FallaHeladeraDTO(heladeraId, LocalDateTime.now()));
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

        Heladera heladera = this.heladeraService.findHeladeraById(heladeraId);
        this.notificadorService.enviarMovimiento(new MovimientoDTO(heladeraId, heladera.getCantidadDeViandas(), heladera.getCantidadDeViandasMaxima()));
    }

    @Override
    public void retirar(RetiroDTO retiroDTO) throws NoSuchElementException {
        ViandaDTO vianda = this.fachadaViandas.buscarXQR(retiroDTO.getQrVianda());
        heladeraService.withdrawVianda(retiroDTO.getHeladeraId());
        this.fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.RETIRADA);

        Heladera heladera = this.heladeraService.findHeladeraById(retiroDTO.getHeladeraId());
        this.notificadorService.enviarMovimiento(new MovimientoDTO(retiroDTO.getHeladeraId(), heladera.getCantidadDeViandas(), heladera.getCantidadDeViandasMaxima()));
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
