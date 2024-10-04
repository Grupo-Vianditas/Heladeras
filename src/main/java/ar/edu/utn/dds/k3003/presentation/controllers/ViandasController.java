package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ContextMappers.ViandaDTOMapper;

import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorHandler;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.ViandasCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import java.util.Map;
import java.util.NoSuchElementException;

public class ViandasController {

    private Fachada fachada;
    private ViandasCounter viandasCounter;
    private ViandaDTOMapper viandaDTOMapper;

    public ViandasController(Fachada fachada, ViandasCounter viandasCounter){
        this.fachada = fachada;
        this.viandasCounter = viandasCounter;
        this.viandaDTOMapper = new ViandaDTOMapper();
    }

    public void depositar(Context ctx) {
        try {
            var viandaDTO = viandaDTOMapper.mapper(ctx); // Eso si que no lo cambio.
            this.fachada.depositar(viandaDTO.getHeladeraId(), viandaDTO.getCodigoQR());
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of("Status", "Done"));
            viandasCounter.incrementSuccessfulPostCounter();
        }catch(NoSuchElementException | IllegalArgumentException e) {
            ErrorHandler.manejarError(ctx, HttpStatus.BAD_REQUEST, 1, "Error de solicitud");
            viandasCounter.incrementFailedPostCounter();
        } catch (Exception e) {
            ErrorHandler.manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 3, "Error no contemplado: " + e);
            viandasCounter.incrementFailedPostCounter();
        }
    }

    public void retirar(Context ctx) {
        try {
            this.fachada.retirar(
                    ctx.bodyAsClass(RetiroDTO.class)
            );
            ctx.status(HttpStatus.OK);
            ctx.json(Map.of("Status", "Done"));
            viandasCounter.incrementSuccessfulGetCounter();
        }catch(NoSuchElementException | IllegalArgumentException e) {
            ErrorHandler.manejarError(ctx, HttpStatus.BAD_REQUEST, 1, "Error de solicitud");
            viandasCounter.incrementFailedGetCounter();
        } catch (Exception e) {
            ErrorHandler.manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 3, "Error no contemplado: " + e);
            viandasCounter.incrementFailedGetCounter();
        }
    }
}
