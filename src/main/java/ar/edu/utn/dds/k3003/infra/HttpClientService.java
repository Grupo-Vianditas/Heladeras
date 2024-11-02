package ar.edu.utn.dds.k3003.infra;

import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.FallaHeladeraDTO;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.MovimientoDTO;
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

    public HttpClientService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        this.urlMovimiento = System.getenv("URL_COLABORADORES_MOVIMIENTO");
        this.urlFallaHeladera = System.getenv("URL_COLABORADORES_FALLA");
    }

    public String enviarMovimiento(MovimientoDTO movimiento) throws Exception {
        return enviarPostRequest(urlMovimiento, movimiento);
    }

    public String enviarFallaHeladera(FallaHeladeraDTO falla) throws Exception {
        return enviarPostRequest(urlFallaHeladera, falla);
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