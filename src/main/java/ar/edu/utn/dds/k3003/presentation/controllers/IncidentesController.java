package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.IncidenteDTO;
import ar.edu.utn.dds.k3003.presentation.controllers.baseController.BaseController;
import ar.edu.utn.dds.k3003.service.MetricsService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;


import javax.validation.ValidationException;

public class IncidentesController extends BaseController {

    private final Fachada fachada;

    public IncidentesController(Fachada fachada, MetricsService metricsService) {
        super(metricsService);
        this.fachada = fachada;
    }

    public void notificarAlerta(Context ctx){
        handleRequest(ctx, "/notificarAlerta", "POST", () -> {
            IncidenteDTO reporteDTO = parseBody(ctx, IncidenteDTO.class);
            validarDTO(reporteDTO);
            ctx.json(this.fachada.generarIncidenteAlerta(reporteDTO));
            ctx.status(HttpStatus.OK);
        });
    }

    // Hecho
    public void falloTecnico(Context ctx) {
        handleRequest(ctx, "/falloTecnico", "POST", () -> {
            IncidenteDTO reporteDTO = parseBody(ctx, IncidenteDTO.class);
            validarDTO(reporteDTO);
            ctx.json(fachada.generarIncidenteTecnico(reporteDTO));
            ctx.status(HttpStatus.CREATED);
        });
    }

    public void getIncidentesByHeladeraId(Context ctx){
        handleRequest(ctx, "/incidentes/{heladeraId}", "GET", () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(fachada.obtenerIncidentes(heladeraId));
            ctx.status(HttpStatus.OK);
        });
    }

    private static void validarDTO(IncidenteDTO reporteDTO) throws ValidationException {
        if (reporteDTO == null) {
            throw new ValidationException("El objeto reporteDTO no puede ser nulo.");
        }
        if (reporteDTO.getHeladeraId() == null || reporteDTO.getHeladeraId() == 0) {
            throw new ValidationException("Debe enviar un Id para reportar a la heladera.");
        }
        if (reporteDTO.getColaboradorId() == null) {
            throw new ValidationException("Debe enviar un Id para reportar al responsable.");
        }
        if (reporteDTO.getDenunciante() == null || reporteDTO.getDenunciante().isBlank()) {
            throw new ValidationException("Debe especificar un denunciante.");
        }
        if (reporteDTO.getTipoIncidente() == null) {
            throw new ValidationException("Debe especificar el tipo de incidente a reportar.");
        }
    }
}

