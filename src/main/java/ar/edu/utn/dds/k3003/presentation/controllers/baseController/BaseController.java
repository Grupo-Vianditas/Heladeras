package ar.edu.utn.dds.k3003.presentation.controllers.baseController;

import ar.edu.utn.dds.k3003.model.errors.OperacionInvalidaException;
import ar.edu.utn.dds.k3003.service.MetricsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.validation.ValidationException;
import org.hibernate.exception.JDBCConnectionException;

import javax.persistence.NoResultException;
import javax.transaction.TransactionalException;
import java.util.Map;

public abstract class BaseController {

    private final ObjectMapper objectMapper;
    private final MetricsService metricsService;

    protected BaseController(MetricsService metricsService) {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.metricsService = metricsService;
    }

    protected <T> T parseBody(Context ctx, Class<T> type) {
        try {
            return objectMapper.readValue(ctx.body(), type);
        } catch (JsonMappingException e) {
            throw new IllegalArgumentException("Error de mapeo JSON. Verifique los valores enviados.", e);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error de formato JSON. Asegúrese de incluir todos los campos requeridos.", e);
        }
    }

    protected void handleRequest(Context ctx, String endpoint, String method, Runnable action) {
        try {
            action.run();
            metricsService.increment(endpoint, method, "successful");
        } catch (IllegalArgumentException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, 1, e.getMessage(), endpoint, method);
        } catch (TransactionalException e) {
            handleError(ctx, HttpStatus.CONFLICT, 1,
                    "Error transaccional: ¿Se está transaccionando bien?", endpoint, method);
        } catch (JDBCConnectionException e) {
            handleError(ctx, HttpStatus.CONFLICT, 2,
                    "Error en el conector JDBC: ¿Está viva la DB?", endpoint, method);
        } catch (javax.validation.ValidationException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, 2, "Error de validación: " + e.getMessage(), endpoint, method);
        } catch (NoResultException e) {
            handleError(ctx, HttpStatus.NOT_FOUND, 3, "No existe una heladera con ese ID.", endpoint, method);
        } catch (javax.persistence.PersistenceException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, 4, "Ya existe una heladera con ese nombre.", endpoint, method);
        } catch (OperacionInvalidaException e) {
            handleError(ctx, HttpStatus.BAD_REQUEST, 5, "Operacion invalida detectada : " + e.getMessage(), endpoint, method);
        }catch(IllegalStateException e){
            handleError(ctx, HttpStatus.BAD_REQUEST, 6, "Estado invalido de la heladera : " + e.getMessage(), endpoint, method);
        } catch (Exception e) {
            handleError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 7, "Error inesperado: " + e.getMessage(), endpoint, method);
        }
    }

    protected void handleError(Context ctx, HttpStatus status, int errorCode, String message, String endpoint, String method) {
        metricsService.increment(endpoint, method, "failed");
        ctx.status(status);
        ctx.json(Map.of(
                "status", "Failed",
                "errorCode", errorCode,
                "message", message
        ));
    }
}
