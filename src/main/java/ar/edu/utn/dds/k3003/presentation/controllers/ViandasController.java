package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;

import ar.edu.utn.dds.k3003.presentation.controllers.baseController.BaseController;
import ar.edu.utn.dds.k3003.service.MetricsService;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;

public class ViandasController extends BaseController {

    private final Fachada fachada;

    public ViandasController(Fachada fachada, MetricsService metricsService) {
        super(metricsService);
        this.fachada = fachada;
    }

    public void depositar(Context ctx) {
        handleRequest(ctx, "/depositar", "POST", () -> {
            ViandaDTO viandaDTO = parseBody(ctx, ViandaDTO.class);
            fachada.depositar(viandaDTO.getHeladeraId(), viandaDTO.getCodigoQR());
            ctx.json(Map.of("Status", "Done"));
            ctx.status(HttpStatus.OK);
        });
    }

    public void retirar(Context ctx) {
        handleRequest(ctx, "/retirar", "POST", () -> {
            RetiroDTO retiroDTO = parseBody(ctx, RetiroDTO.class);
            fachada.retirar(retiroDTO);
            ctx.json(Map.of("Status", "Done"));
            ctx.status(HttpStatus.OK);
        });
    }
}
