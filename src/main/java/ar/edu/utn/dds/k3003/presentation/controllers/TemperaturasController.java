package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorResponse;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.TemperaturasCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.time.DateTimeException;
import java.util.NoSuchElementException;

public class TemperaturasController {

    private Fachada fachada;
    private TemperaturasCounter temperaturasCounter;

    public TemperaturasController(Fachada fachada, TemperaturasCounter temperaturasCounter){
        this.fachada = fachada;
        this.temperaturasCounter = temperaturasCounter;
    }

    public void registrarTemperatura(Context ctx) {
        try {
            this.fachada.temperatura(
                    ctx.bodyAsClass(TemperaturaDTO.class)
            );
            ctx.result("Temperatura registrada correctamente");
            ctx.status(HttpStatus.OK);
            temperaturasCounter.incrementSuccessfulPostCounter();
        }catch(NoSuchElementException | IllegalArgumentException | DateTimeException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
            temperaturasCounter.incrementSuccessfulPostCounter();
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint registrarTemperatura: "+e));
            temperaturasCounter.incrementSuccessfulPostCounter();
        }
    }

    public void obtenerTemperaturas(Context ctx) {
        try {
            ctx.json(
                    this.fachada.obtenerTemperaturas(
                            ctx.pathParamAsClass("heladeraId", Integer.class).get()
                    )
            );
            ctx.status(HttpStatus.OK);
            temperaturasCounter.incrementSuccessfulGetCounter();
        }catch(NoSuchElementException e) {
            ctx.status(HttpStatus.NOT_FOUND);
            ctx.json("Heladera no encontrada");
            temperaturasCounter.incrementFailedGetCounter();
        }catch(IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(1, e.getMessage()));
            temperaturasCounter.incrementFailedGetCounter();
        }catch(io.javalin.validation.ValidationException e){
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(new ErrorResponse(2, "Se envio un valor no valido como Id"));
            temperaturasCounter.incrementFailedGetCounter();
        }catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint obtenerTemperaturas: "+e));
            temperaturasCounter.incrementFailedGetCounter();
        }
    }

}
