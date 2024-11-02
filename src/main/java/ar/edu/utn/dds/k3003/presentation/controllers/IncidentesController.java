package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.model.incidentes.subtipos.SubtipoAlerta;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorHandler;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.IncidentesCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.persistence.NoResultException;

public class IncidentesController {

    private final Fachada fachada;
    private final IncidentesCounter incidentesCounter;

    public IncidentesController(Fachada fachada, IncidentesCounter incidentesCounter) {
        this.fachada = fachada;
        this.incidentesCounter = incidentesCounter;
    }

    public void alertarTemperatura(Context ctx){
        handleRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.generarIncidenteAlerta(heladeraId, SubtipoAlerta.TEMPERATURA_FUERA_DE_RANGO));
            ctx.status(HttpStatus.OK);
            incidentesCounter.incrementSucessfulPostCounter();
        });
    }

    public void alertarFraude(Context ctx){
        handleRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.generarIncidenteAlerta(heladeraId, SubtipoAlerta.FRAUDE_DE_MOVIMIENTO));
            ctx.status(HttpStatus.OK);
            incidentesCounter.incrementSucessfulPostCounter();
        });
    }

    public void alertarConexion(Context ctx){
        handleRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.generarIncidenteAlerta(heladeraId, SubtipoAlerta.FALLA_DE_CONEXION));
            ctx.status(HttpStatus.OK);
            incidentesCounter.incrementSucessfulPostCounter();
        });
    }

    public void falloTecnico(Context ctx){
        handleRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.generarIncidenteTecnico(heladeraId));
            ctx.status(HttpStatus.OK);
            incidentesCounter.incrementSucessfulPostCounter();
        });
    }

    private void handleRequest(Context ctx, Runnable action) {
        try {
            action.run();
        } catch (NoResultException e) {
            manejarError(ctx, HttpStatus.NOT_FOUND, 1, "No existe una heladera con ese Id.");
        } catch (io.javalin.validation.ValidationException e) {
            manejarError(ctx, HttpStatus.BAD_REQUEST, 2, "Se envió un valor inválido como Id.");
        } catch (java.lang.IllegalArgumentException | IllegalStateException e) {
            manejarError(ctx, HttpStatus.BAD_REQUEST, 3, e.getMessage());
        } catch (Exception e) {
            manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 4, "Error no contemplado: " + e);
        }
    }

    private void manejarError(Context ctx, HttpStatus status, int errorCode, String message) {
        ErrorHandler.manejarError(ctx, status, errorCode, message);
    }
}
