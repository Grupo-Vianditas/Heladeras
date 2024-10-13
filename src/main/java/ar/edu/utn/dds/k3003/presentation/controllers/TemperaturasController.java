package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.TemperaturaDTO;

import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorHandler;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.TemperaturasCounter;

import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.persistence.NoResultException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.Map;
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

            boolean flag = false;

            TemperaturaDTO receivedTemp = ctx.bodyAsClass(TemperaturaDTO.class);

            // Si alguien manda una temperatura sin fecha, somos buenos y le asignamos 1
            if (receivedTemp.getFechaMedicion() == null){
                receivedTemp.setFechaMedicion(LocalDateTime.now());
                flag = true;
            }

            this.fachada.temperatura(receivedTemp);
            if (!flag){
                ctx.json(Map.of("Status", "Done"));
            } else{
                ctx.json(Map.of("Status", "Done with date creation"));
            }
            ctx.status(HttpStatus.OK);
            temperaturasCounter.incrementSuccessfulPostCounter();

        }catch(NoResultException e) {
            ErrorHandler.manejarError(ctx, HttpStatus.BAD_REQUEST, 1, "No existe una heladera con ese Id.");
            temperaturasCounter.incrementFailedPostCounter();
        } catch (Exception e) {
            ErrorHandler.manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 3, "Error no contemplado: " + e);
            temperaturasCounter.incrementFailedPostCounter();
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
            ErrorHandler.manejarError(ctx, HttpStatus.NOT_FOUND, 1, "No existe una heladera con ese Id.");
            temperaturasCounter.incrementFailedGetCounter();
        }catch(IllegalArgumentException e) {
            ErrorHandler.manejarError(ctx, HttpStatus.BAD_REQUEST, 2, "Error en la parametrizacion del request.");
            temperaturasCounter.incrementFailedGetCounter();
        }catch(io.javalin.validation.ValidationException e){
            ErrorHandler.manejarError(ctx, HttpStatus.BAD_REQUEST, 2, "Se envio un Id invalido.");
            temperaturasCounter.incrementFailedGetCounter();
        }catch (Exception e) {
            ErrorHandler.manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 3, "Error no contemplado: " + e);
            temperaturasCounter.incrementFailedGetCounter();
        }
    }

}
