package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.model.Retiro;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.FallaHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.HabilitacionDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.MovimientoDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.IncidenteDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.CreateHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.ReturningHeladeraDTO;
import ar.edu.utn.dds.k3003.service.*;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Getter
public class Fachada implements ar.edu.utn.dds.k3003.facades.FachadaHeladeras{

    private FachadaViandas fachadaViandas;

    private final HeladeraService heladeraService;
    private final TemperaturaService temperaturaService;
    private final IncidentesService incidentesService;
    private final ImpresionService impresionService;
    private final NotificadorService notificadorService;
    private final RetirosService retirosService;

    public Fachada(HeladeraService heladeraService, TemperaturaService temperaturaService, IncidentesService incidentesService, ImpresionService impresionService, NotificadorService notificadorService, RetirosService retirosService){
        this.heladeraService = heladeraService;
        this.temperaturaService = temperaturaService;
        this.incidentesService = incidentesService;
        this.impresionService = impresionService;
        this.notificadorService = notificadorService;
        this.retirosService = retirosService;
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
    public HabilitacionDTO habilitar(IncidenteDTO reporte){
        HabilitacionDTO habilitacion = heladeraService.habilitar(reporte.getHeladeraId());

        Incidente incidente = incidentesService.generarIncidente(reporte);
        incidentesService.save(incidente);
        impresionService.imprimirIncidente(incidente);

        return habilitacion;
    }

    public HabilitacionDTO deshabilitar(IncidenteDTO reporte){
        HabilitacionDTO habilitacion = heladeraService.deshabilitar(reporte.getHeladeraId());

        Incidente incidente = incidentesService.generarIncidente(reporte);
        incidentesService.save(incidente);
        impresionService.imprimirIncidente(incidente);

        notificadorService.enviarFalla(new FallaHeladeraDTO(reporte.getHeladeraId(), LocalDateTime.now()));

        return habilitacion;
    }

    public HabilitacionDTO generarIncidenteAlerta(IncidenteDTO reporte){
        HabilitacionDTO nuevoEstado = heladeraService.deshabilitar(reporte.getHeladeraId());
        Incidente incidente = incidentesService.generarIncidente(reporte);
        incidentesService.save(incidente);
        impresionService.imprimirIncidente(incidente);

        return nuevoEstado;
    }

    public HabilitacionDTO generarIncidenteTecnico(IncidenteDTO reporteDTO){
        HabilitacionDTO nuevoEstado = heladeraService.deshabilitar(reporteDTO.getHeladeraId());
        Incidente incidente = incidentesService.generarIncidente(reporteDTO);
        incidentesService.save(incidente);
        impresionService.imprimirIncidente(incidente);
        notificadorService.enviarFalla(new FallaHeladeraDTO(incidente.getHeladeraId(), incidente.getTimestamp()));
        return nuevoEstado;
    }

    @Override
    public Integer cantidadViandas(Integer heladeraId) throws NoSuchElementException {
        return heladeraService.getCantidadViandas(heladeraId);
    }

    @Override
    public void depositar(Integer heladeraId, String qrVianda) throws NoSuchElementException {
        ViandaDTO vianda = fachadaViandas.buscarXQR(qrVianda);
        heladeraService.depositVianda(heladeraId);
        fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.DEPOSITADA);

        Heladera heladera = heladeraService.findHeladeraById(heladeraId);
        notificadorService.enviarMovimiento(new MovimientoDTO(heladeraId, heladera.getCantidadDeViandas(), heladera.getCantidadDeViandasMaxima()));
    }

    @Override
    public void retirar(RetiroDTO retiroDTO) throws NoSuchElementException {
        ViandaDTO vianda = this.fachadaViandas.buscarXQR(retiroDTO.getQrVianda());
        heladeraService.withdrawVianda(retiroDTO.getHeladeraId());
        fachadaViandas.modificarEstado(vianda.getCodigoQR(), EstadoViandaEnum.RETIRADA);

        retirosService.save(new Retiro(retiroDTO.getHeladeraId(), retiroDTO.getQrVianda(), retiroDTO.getFechaRetiro()));

        Heladera heladera = this.heladeraService.findHeladeraById(retiroDTO.getHeladeraId());
        notificadorService.enviarMovimiento(new MovimientoDTO(retiroDTO.getHeladeraId(), heladera.getCantidadDeViandas(), heladera.getCantidadDeViandasMaxima()));
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

    public List<IncidenteDTO> obtenerIncidentes(Integer heladeraId){
        return incidentesService.getIncidentesDTO(
                incidentesService.getIncidenteByHeladeraId(heladeraId)
        );
    }

    public void purgarTodo(){
        incidentesService.clear();
        temperaturaService.clear();
        heladeraService.clear();
        retirosService.clear();
    }

    @Override
    public void setViandasProxy(FachadaViandas viandas){
        this.fachadaViandas = viandas;
    }

    public List<Retiro> retirosDelDia(Integer heladeraId) {
        return retirosService.getDailyRetirosByHeladeraId(heladeraId);
    }
}
