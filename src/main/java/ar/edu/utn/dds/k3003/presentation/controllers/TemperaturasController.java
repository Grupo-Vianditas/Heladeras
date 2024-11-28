package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;

import ar.edu.utn.dds.k3003.presentation.controllers.baseController.BaseController;

import ar.edu.utn.dds.k3003.service.MetricsService;

import io.javalin.http.Context;

import java.time.LocalDateTime;
import java.util.Map;

public class TemperaturasController extends BaseController {

    private final Fachada fachada;

    public TemperaturasController(Fachada fachada, MetricsService metricsService) {
        super(metricsService);
        this.fachada = fachada;
    }

    public void registrarTemperatura(Context ctx) {
        handleRequest(ctx, "/temperaturas", "POST", () -> {
            TemperaturaDTO temperaturaDTO = parseBody(ctx, TemperaturaDTO.class);
            boolean fechaAsignada = asignarFechaSiEsNecesario(temperaturaDTO);

            fachada.temperatura(temperaturaDTO);

            ctx.json(Map.of(
                    "status", "Success",
                    "message", fechaAsignada ? "Fecha asignada automáticamente" : "Temperatura registrada"
            ));
        });
    }

    public void obtenerTemperaturas(Context ctx) {
        handleRequest(ctx, "/temperaturas/{heladeraId}", "GET", () -> {
            int heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(fachada.obtenerTemperaturas(heladeraId));
        });
    }

    private boolean asignarFechaSiEsNecesario(TemperaturaDTO temperaturaDTO) {
        if (temperaturaDTO.getFechaMedicion() == null) {
            temperaturaDTO.setFechaMedicion(LocalDateTime.now());
            return true;
        }
        return false;
    }
}
