package ar.edu.utn.dds.k3003.infra;

import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.FallaHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.MovimientoDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.ReparacionHeladeraHeladeraDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String urlMovimiento;
    private final String urlFallaHeladera;
    private final String urlReparacion;

    public HttpClientService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.urlMovimiento = "https://two024-tp-entrega-2-emilianosalvano.onrender.com/eventos/movimientoDeViandaEnHeladera";
        this.urlFallaHeladera = "https://two024-tp-entrega-2-emilianosalvano.onrender.com/eventos/fallaHeladera";
        this.urlReparacion = "https://two024-tp-entrega-2-emilianosalvano.onrender.com/colaboradores/reparacionDeHeladera";
    }

    public String enviarMovimiento(MovimientoDTO movimiento) throws Exception {
        return enviarPostRequest(urlMovimiento, movimiento);
    }

    public String enviarFallaHeladera(FallaHeladeraDTO falla) throws Exception {
        return enviarPostRequest(urlFallaHeladera, falla);
    }

    public String enviarReparacionHeladera(ReparacionHeladeraHeladeraDTO reparacionHeladera) throws Exception{
        return enviarPostRequest(urlReparacion, reparacionHeladera);
    }

    private <T> String enviarPostRequest(String url, T dto) throws Exception {
        String requestBody = objectMapper.writeValueAsString(dto);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {
            throw new RuntimeException("El envio fallo con el siguiente error: " + response.statusCode());
        }
    }
}