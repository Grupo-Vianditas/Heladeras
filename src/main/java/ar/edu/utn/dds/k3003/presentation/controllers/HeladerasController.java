package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;

import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorHandler;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.HeladerasCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.persistence.NoResultException;

public class HeladerasController {

    private final Fachada fachada;
    private final HeladerasCounter heladerasCounter;

    public HeladerasController(Fachada fachada, HeladerasCounter heladerasCounter) {
        this.fachada = fachada;
        this.heladerasCounter = heladerasCounter;
    }

    public void agregar(Context ctx) {
        handleRequest(ctx, () -> {
            HeladeraDTO heladeraDTO = ctx.bodyAsClass(HeladeraDTO.class);
            ctx.json(this.fachada.agregar(heladeraDTO));
            ctx.status(HttpStatus.CREATED);
            heladerasCounter.incrementSucessfulPostCounter();
        });
    }

    public void buscarXId(Context ctx) {
        handleRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.buscarXId(heladeraId));
            ctx.status(HttpStatus.OK);
            heladerasCounter.incrementSuccessfulGetCounter();
        });
    }

    public void habilitar(Context ctx) {
        handleRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.habilitar(heladeraId));
            ctx.status(HttpStatus.OK);
            heladerasCounter.incrementSucessfulPostCounter();
        });
    }

    public void inhabilitar(Context ctx) {
        handleRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(this.fachada.inhabilitar(heladeraId));
            ctx.status(HttpStatus.OK);
            heladerasCounter.incrementSucessfulPostCounter();
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
        heladerasCounter.incrementFailedPostCounter();
    }
}

