package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.HeladeraDTO;
import ar.edu.utn.dds.k3003.model.errors.OperacionInvalidaException;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorHandler;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.HeladerasCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.hibernate.boot.model.naming.IllegalIdentifierException;

import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;


public class HeladerasController {

    private Fachada fachada;
    private HeladerasCounter heladerasCounter;

    public HeladerasController(Fachada fachada, HeladerasCounter heladerasCounter){
        this.fachada = fachada;
        this.heladerasCounter = heladerasCounter;
    }

    // ################
    // Update 4/10/24: respuesta de controller:
    // 1: Se intento insertar una heladera con un Id a mano.
    // 2: Cambia el nombre de la heladera capo, es unique en la db.
    // 3: Otros errores
    // ################

    public void agregar(Context ctx) {
        try {
            ctx.json(
                    this.fachada.agregar(
                            ctx.bodyAsClass(HeladeraDTO.class)
                    )
            );
            ctx.status(HttpStatus.CREATED);
            heladerasCounter.incrementSucessfulPostCounter();
        }catch(OperacionInvalidaException e){
            ErrorHandler.manejarError(ctx, HttpStatus.BAD_REQUEST, 1, "No se puede crear una heladera con mas de 100 viandas.");
            heladerasCounter.incrementFailedPostCounter();
        }catch(IllegalArgumentException e) {
            ErrorHandler.manejarError(ctx, HttpStatus.BAD_REQUEST, 1, "No se pueden asignar Ids de forma manual.");
            heladerasCounter.incrementFailedPostCounter();
        }catch(PersistenceException e) {
            ErrorHandler.manejarError(ctx, HttpStatus.BAD_REQUEST, 2, "Cambie el nombre de la heladera a persistir.");
            heladerasCounter.incrementFailedPostCounter();
        } catch (Exception e) {
            ErrorHandler.manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 3, "Error no contemplado: " + e);
            heladerasCounter.incrementFailedPostCounter();
        }
    }


    // ################
    // Update 4/10/24: respuesta de controller:
    // 1: Se intento insertar una heladera con un Id a mano.
    // 2: Cambia el nombre de la heladera capo, es unique en la db.
    // 3: Otros errores
    // ################

    public void buscarXId(Context ctx) {
        try {
            ctx.json(
                    this.fachada.buscarXId(
                            ctx.pathParamAsClass("heladeraId", Integer.class).get()
                    )
            );
            ctx.status(HttpStatus.OK);
            heladerasCounter.incrementSuccessfulGetCounter();
        }catch(NoResultException e) {
            ErrorHandler.manejarError(ctx, HttpStatus.NOT_FOUND, 1, "No existe una heladera con ese Id.");
            heladerasCounter.incrementFailedGetCounter();
        }catch(io.javalin.validation.ValidationException e){
            ErrorHandler.manejarError(ctx, HttpStatus.BAD_REQUEST, 2, "Se envio un valor invalido como Id.");
            heladerasCounter.incrementFailedGetCounter();
        }catch (Exception e) {
            ErrorHandler.manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 3, "Error no contemplado: " + e);
            heladerasCounter.incrementFailedGetCounter();
        }
    }

}
