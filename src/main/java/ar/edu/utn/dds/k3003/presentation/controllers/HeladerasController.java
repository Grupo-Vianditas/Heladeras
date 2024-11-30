package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;

import ar.edu.utn.dds.k3003.model.incidente.TipoIncidenteEnum;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.IncidenteDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.CreateHeladeraDTO;

import ar.edu.utn.dds.k3003.presentation.controllers.baseController.BaseController;
import ar.edu.utn.dds.k3003.service.MetricsService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.validation.ValidationException;

public class HeladerasController extends BaseController {

    private final Fachada fachada;

    public HeladerasController(Fachada fachada, MetricsService metricsService) {
        super(metricsService);
        this.fachada = fachada;
    }

    //Validado
    public void agregar(Context ctx) {
        handleRequest(ctx, "/heladeras", "POST", () -> {
            CreateHeladeraDTO heladeraDTO = parseBody(ctx, CreateHeladeraDTO.class);
            validarCreateHeladeraDTO(heladeraDTO);
            ctx.json(fachada.agregar(heladeraDTO));
            ctx.status(HttpStatus.CREATED);
        });
    }

    public void buscarXId(Context ctx) {
        handleRequest(ctx, "/heladeras/{heladeraId}", "GET", () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(fachada.buscarXId(heladeraId));
            ctx.status(HttpStatus.OK);
        });
    }

    public void habilitar(Context ctx) {
        handleRequest(ctx, "/heladeras/habilitar", "POST", () -> {
            IncidenteDTO reporte = parseBody(ctx, IncidenteDTO.class);
            if(validarIncidenteDTO(reporte, TipoIncidenteEnum.REPARACION)) {
                ctx.json(fachada.habilitar(reporte));
                ctx.status(HttpStatus.OK);
            } else {
                throw new javax.validation.ValidationException("El cuerpo del mensaje es invalido.");
            }
        });
    }

    public void deshabilitar(Context ctx) {
        handleRequest(ctx, "/heladeras/deshabilitar", "POST", () -> {
            IncidenteDTO reporte = parseBody(ctx, IncidenteDTO.class);
            if(validarIncidenteDTO(reporte, TipoIncidenteEnum.FALLA_TECNICA)) {
                ctx.json(fachada.deshabilitar(reporte));
                ctx.status(HttpStatus.OK);
            }else {
                throw new javax.validation.ValidationException("El cuerpo del mensaje es invalido.");
            }
        });
    }

    private static boolean validarIncidenteDTO(IncidenteDTO reporte, TipoIncidenteEnum tipoEsperado){
        return reporte.getTipoIncidente() == tipoEsperado;
    }


    private static void validarCreateHeladeraDTO(CreateHeladeraDTO heladeraDTO) throws ValidationException {
        if (heladeraDTO == null) {
            throw new ValidationException("El objeto HeladeraDTO no puede ser nulo.");
        }
        if (heladeraDTO.getNombre() == null || heladeraDTO.getNombre().isBlank()) {
            throw new ValidationException("La heladera debe tener un nombre válido.");
        }
        if (heladeraDTO.getCantidadDeViandas() == null || heladeraDTO.getCantidadDeViandas() < 0) {
            throw new ValidationException("La cantidad de viandas debe ser un número no negativo.");
        }
        if (heladeraDTO.getHabilitacion() == null) {
            throw new ValidationException("El estado de habilitación no puede ser nulo.");
        }
    }
}
