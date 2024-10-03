package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorResponse;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.OthersCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class CleanerController {

    private Fachada fachada;
    private OthersCounter cleanerCounter;

    public CleanerController(Fachada fachada, OthersCounter cleanerCounter) {
        this.fachada = fachada;
        this.cleanerCounter = cleanerCounter;
    }

    public void clear(Context ctx){
        try{
            this.fachada.purgarTodo();
            ctx.status(HttpStatus.OK);
            cleanerCounter.incrementSuccessfulClearCounter();
        }catch(IllegalArgumentException e){
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
            cleanerCounter.incrementFailedClearCounter();
        }catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint clear: "+e));
            cleanerCounter.incrementFailedClearCounter();
        }

    }
}