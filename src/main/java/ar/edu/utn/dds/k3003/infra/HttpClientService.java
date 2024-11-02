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

    public HttpClientService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public String enviarMovimiento(MovimientoDTO movimiento) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(movimiento);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(System.getenv().get("URL_COLABORADORES_MOVIMIENTO")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String enviarFallaHeladera(FallaHeladeraDTO falla) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String requestBody = objectMapper.writeValueAsString(falla);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(System.getenv().get("URL_COLABORADORES_FALLA")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }


}