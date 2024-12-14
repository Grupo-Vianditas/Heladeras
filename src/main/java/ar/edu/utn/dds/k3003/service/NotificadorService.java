package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.infra.HttpClientService;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.FallaHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.MovimientoDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.ReparacionHeladeraHeladeraDTO;

import java.util.function.Supplier;

public class NotificadorService {
    private final HttpClientService httpClientService;

    public NotificadorService(HttpClientService httpClientService) {
        this.httpClientService = httpClientService;
    }

    public NotificadorService() {
        this(new HttpClientService());
    }

    public void enviarMovimiento(MovimientoDTO movimiento) {
        ejecutarNotificacion(() -> {
            try {
                return httpClientService.enviarMovimiento(movimiento);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void enviarFalla(FallaHeladeraDTO falla) {
        ejecutarNotificacion(() -> {
            try {
                return httpClientService.enviarFallaHeladera(falla);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void enviarReparacion(ReparacionHeladeraHeladeraDTO reparacionHeladeraHeladeraDTO) {
        ejecutarNotificacion(() -> {
            try {
                return httpClientService.enviarReparacionHeladera(reparacionHeladeraHeladeraDTO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void ejecutarNotificacion(Supplier<String> operacion) {
        try {
            String respuesta = operacion.get();
            logRespuesta(respuesta);
        } catch (Exception e) {
            manejarError(e);
        }
    }

    private void logRespuesta(String respuesta) {
        System.out.println("Respuesta del servidor: " + respuesta);
    }

    private void manejarError(Exception e) {
        System.err.println("Error al enviar notificación: " + e.getMessage());
        e.printStackTrace();
    }
}
