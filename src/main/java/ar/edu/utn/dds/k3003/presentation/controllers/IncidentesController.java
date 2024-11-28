package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.model.incidentes.subtipos.SubtipoAlerta;
import ar.edu.utn.dds.k3003.presentation.controllers.baseController.BaseController;
import ar.edu.utn.dds.k3003.service.MetricsService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.persistence.NoResultException;

public class IncidentesController extends BaseController {

    private final Fachada fachada;

    public IncidentesController(Fachada fachada, MetricsService metricsService) {
        super(metricsService);
        this.fachada = fachada;
    }

    public void alertarTemperatura(Context ctx) {
        handleRequest(ctx, "/alertarTemperatura", "POST", () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.generarIncidenteAlerta(heladeraId, SubtipoAlerta.TEMPERATURA_FUERA_DE_RANGO));
            ctx.status(HttpStatus.OK);
        });
    }

    public void alertarFraude(Context ctx) {
        handleRequest(ctx, "/alertarFraude", "POST", () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.generarIncidenteAlerta(heladeraId, SubtipoAlerta.FRAUDE_DE_MOVIMIENTO));
            ctx.status(HttpStatus.OK);
        });
    }

    public void alertarConexion(Context ctx) {
        handleRequest(ctx, "/alertarConexion", "POST", () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.generarIncidenteAlerta(heladeraId, SubtipoAlerta.FALLA_DE_CONEXION));
            ctx.status(HttpStatus.OK);
        });
    }

    public void falloTecnico(Context ctx) {
        handleRequest(ctx, "/falloTecnico", "POST", () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.generarIncidenteTecnico(heladeraId));
            ctx.status(HttpStatus.OK);
        });
    }
}

