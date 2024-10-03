package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorResponse;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.HeladerasCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.hibernate.boot.model.naming.IllegalIdentifierException;

import java.util.NoSuchElementException;

public class HeladerasController {

    private Fachada fachada;
    private HeladerasCounter heladerasCounter;

    public HeladerasController(Fachada fachada, HeladerasCounter heladerasCounter){
        this.fachada = fachada;
        this.heladerasCounter = heladerasCounter;
    }

    public void agregar(Context ctx) {
        try {
            ctx.json( // 3° Retorno el json con el DTO
                    this.fachada.agregar( // 2° Debería poder agregar esa heladeraDTO a la fachada
                            ctx.bodyAsClass(HeladeraDTO.class) // 1° Parseo el body del ctx a HeladeraDTO
                    )
            );
            ctx.status(HttpStatus.CREATED);
            heladerasCounter.incrementSucessfulPostCounter();
        }catch(IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
            heladerasCounter.incrementFailedPostCounter();
        }catch(IllegalIdentifierException e) {
            ctx.status(HttpStatus.NOT_ACCEPTABLE);
            ctx.json(new ErrorResponse(2, e.getMessage()));
            heladerasCounter.incrementFailedPostCounter();
        }catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint agregar: "+e));
            heladerasCounter.incrementFailedPostCounter();
        }
    }

    public void buscarXId(Context ctx) {
        try {
            ctx.json(
                    this.fachada.buscarXId(
                            ctx.pathParamAsClass("heladeraId", Integer.class).get()
                    )
            );
            ctx.status(HttpStatus.OK);
            heladerasCounter.incrementSuccessfulGetCounter();
        }catch(NoSuchElementException e) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json(new ErrorResponse(0, e.getMessage()));
            heladerasCounter.incrementFailedGetCounter();
        }catch(IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
            heladerasCounter.incrementFailedGetCounter();
        }catch(io.javalin.validation.ValidationException e){
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(2, "Se envio un valor no valido como Id"));
            heladerasCounter.incrementFailedGetCounter();
        }catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint buscarXId: "+e));
            heladerasCounter.incrementFailedGetCounter();
        }
    }

}
