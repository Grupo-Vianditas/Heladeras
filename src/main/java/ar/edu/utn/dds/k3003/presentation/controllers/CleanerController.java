package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.presentation.controllers.baseController.BaseController;
import ar.edu.utn.dds.k3003.service.MetricsService;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.hibernate.exception.JDBCConnectionException;

import javax.transaction.TransactionalException;
import java.util.Map;

public class CleanerController extends BaseController {

    private final Fachada fachada;

    public CleanerController(Fachada fachada, MetricsService metricsService) {
        super(metricsService);
        this.fachada = fachada;
    }

    public void clear(Context ctx) {
        handleRequest(ctx, "/cleaner/clear", "POST", () -> {
            fachada.purgarTodo();
            ctx.json(Map.of("Status", "Done"));
            ctx.status(HttpStatus.OK);
        });
    }
}
