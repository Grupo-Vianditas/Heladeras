package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RetiroDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ContextMappers.ViandaDTOMapper;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorResponse;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.ViandasCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

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
            ctx.result("Vianda depositada correctamente");
            viandasCounter.incrementSuccessfulPostCounter();
        }catch(NoSuchElementException | IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.result("Error de solicitud"); // No lo puedo cambiar por definicion de la API
            viandasCounter.incrementFailedPostCounter();
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint depositar: "+e));
            viandasCounter.incrementFailedPostCounter();
        }
    }

    public void retirar(Context ctx) {
        try {
            this.fachada.retirar(
                    ctx.bodyAsClass(RetiroDTO.class)
            );
            ctx.status(HttpStatus.OK);
            ctx.result("Vianda retirada correctamente");
            viandasCounter.incrementSuccessfulGetCounter();
        }catch(NoSuchElementException | IllegalArgumentException e) {
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.result("Error de solicitud"); // IDEM caso anterior...
            viandasCounter.incrementFailedGetCounter();
        } catch (Exception e) {
            ctx.status(HttpStatus.INTERNAL_SERVER_ERROR);
            ctx.json(new ErrorResponse(99, "Ups, hubo un error en el endpoint retirar: "+e));
            viandasCounter.incrementFailedGetCounter();
        }
    }
}
