package ar.edu.utn.dds.k3003.presentation.controllers;

import ar.edu.utn.dds.k3003.app.Fachada;

import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.heladera.CreateHeladeraDTO;

import ar.edu.utn.dds.k3003.presentation.metrics.controllersCounters.HeladerasCounter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.persistence.NoResultException;
import javax.validation.ValidationException;
import java.util.Map;

public class HeladerasController {

    private final Fachada fachada;
    private final HeladerasCounter heladerasCounter;
    private final ObjectMapper objectMapper;

    public HeladerasController(Fachada fachada, HeladerasCounter heladerasCounter) {
        this.fachada = fachada;
        this.heladerasCounter = heladerasCounter;
        this.objectMapper = new ObjectMapper();
    }

    public void agregar(Context ctx) {
        handlePostRequest(ctx, () -> {
            CreateHeladeraDTO heladeraDTO = parseBody(ctx, CreateHeladeraDTO.class);
            validarCreateHeladeraDTO(heladeraDTO);
            ctx.json(fachada.agregar(heladeraDTO));
            ctx.status(HttpStatus.CREATED);
        });
    }

    public void buscarXId(Context ctx) {
        handleGetRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(fachada.buscarXId(heladeraId));
            ctx.status(HttpStatus.OK);
        });
    }

    public void habilitar(Context ctx) {
        handlePostRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(fachada.habilitar(heladeraId));
            ctx.status(HttpStatus.OK);
        });
    }

    public void inhabilitar(Context ctx) {
        handlePostRequest(ctx, () -> {
            Integer heladeraId = ctx.pathParamAsClass("heladeraId", Integer.class).get();
            ctx.json(fachada.inhabilitar(heladeraId));
            ctx.status(HttpStatus.OK);
        });
    }

    private <T> T parseBody(Context ctx, Class<T> type) {
        try {
            return objectMapper.readValue(ctx.body(), type);
        } catch (JsonMappingException e) {
            // El error de mapeo pasa cuando mandas un enum invalido
            throw new IllegalArgumentException("Error de mapeo JSON. Se enviaron campos validos en el DTO?");
        } catch (JsonProcessingException e) {
            // El error de mapeo pasa cuando no mandas bien el DTO
            throw new IllegalArgumentException("Error de formato JSON. Revisar que no falten campos en el DTO.");
        }
    }

    private void handlePostRequest(Context ctx, Runnable action) {
        try {
            action.run();
            heladerasCounter.incrementSucessfulPostCounter();
        } catch (IllegalArgumentException e) {
            manejarError(ctx, HttpStatus.BAD_REQUEST, 1, e.getMessage());
        } catch (ValidationException e) {
            manejarError(ctx, HttpStatus.BAD_REQUEST, 2, "Error de validación: " + e.getMessage());
        } catch (javax.persistence.PersistenceException e){
            manejarError(ctx, HttpStatus.BAD_REQUEST, 3, "Ya existe una heladera con ese nombre.");
        } catch (Exception e) {
            manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 4, "Error inesperado: " + e.getMessage());
        }
    }

    private void handleGetRequest(Context ctx, Runnable action) {
        try {
            action.run();
            heladerasCounter.incrementSuccessfulGetCounter();
        } catch (NoResultException e) {
            manejarError(ctx, HttpStatus.NOT_FOUND, 4, "No existe una heladera con ese Id.");
        } catch (Exception e) {
            manejarError(ctx, HttpStatus.INTERNAL_SERVER_ERROR, 5, "Error inesperado: " + e.getMessage());
        }
    }

    // Validación del DTO
    private static void validarCreateHeladeraDTO(CreateHeladeraDTO heladeraDTO) throws ValidationException {
        if (heladeraDTO == null) {
            throw new ValidationException("El objeto HeladeraDTO no puede ser nulo.");
        }
        if (heladeraDTO.getNombre() == null || heladeraDTO.getNombre().isEmpty()) {
            throw new ValidationException("La heladera debe tener un nombre propio.");
        }

        if (heladeraDTO.getCantidadDeViandas() == null) {
            throw new ValidationException("La cantidad de viandas no puede ser nula.");
        }

        if (heladeraDTO.getCantidadDeViandas() < 0) {
            throw new ValidationException("La cantidad de viandas no puede ser negativa.");
        }

        if (heladeraDTO.getHabilitacion() == null) {
            throw new ValidationException("El estado de habilitacion no puede ser nulo.");
        }
    }

    // Manejo centralizado de errores
    private void manejarError(Context ctx, HttpStatus status, int errorCode, String message) {
        ctx.status(status);
        ctx.json(Map.of("status", "failed", "erorCode", errorCode, "message", message));
        heladerasCounter.incrementFailedPostCounter();
    }
}
