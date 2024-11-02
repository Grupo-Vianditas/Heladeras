package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.infra.HttpClientService;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.FallaHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.MovimientoDTO;

public class NotificadorService {
    private final HttpClientService httpClientService;

    public NotificadorService() {
        this.httpClientService = new HttpClientService();
    }

    public void enviarMovimiento(MovimientoDTO movimiento) {
        try {
            String respuesta = httpClientService.enviarMovimiento(movimiento);
            System.out.println("Respuesta del servidor: " + respuesta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarFalla(FallaHeladeraDTO falla) {
        try {
            String respuesta = httpClientService.enviarFallaHeladera(falla) ;
            System.out.println("Respuesta del servidor: " + respuesta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
