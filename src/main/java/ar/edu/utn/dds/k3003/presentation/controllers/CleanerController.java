package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.presentation.auxiliar.ErrorHandler;
import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.OthersCounter;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.hibernate.exception.JDBCConnectionException;

import javax.transaction.TransactionalException;
import java.util.Map;

public class CleanerController {

    private final Fachada fachada;
    private final OthersCounter cleanerCounter;

    public CleanerController(Fachada fachada, OthersCounter cleanerCounter) {
        this.fachada = fachada;
        this.cleanerCounter = cleanerCounter;
    }

    // ################
    // Update 4/10/24: respuesta de controller:
    // 0: No existe, to do 10
    // 1: Chocamos, se pincho la transacción
    // 2: Muy probablemente se cayo la DB
    // 3: Otro error
    // ################

    public void clear(Context ctx){
        try{
            this.fachada.purgarTodo();
            ctx.json(Map.of("Status", "Done"));
            ctx.status(HttpStatus.OK);
            cleanerCounter.incrementSuccessfulClearCounter();
        }catch(TransactionalException e){
            ErrorHandler.manejarError(ctx, HttpStatus.CONFLICT, 1, "Error transaccional: ¿Se esta transaccionando bien?");
            cleanerCounter.incrementFailedClearCounter();
        }catch(JDBCConnectionException e){
            ErrorHandler.manejarError(ctx, HttpStatus.CONFLICT, 1, "Error en el conector JDBC: ¿Esta viva la DB?");
            cleanerCounter.incrementFailedClearCounter();
        }catch (Exception e) {
            ErrorHandler.manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 3, "Error no contemplado: "+e);
            cleanerCounter.incrementFailedClearCounter();
        }

    }
}