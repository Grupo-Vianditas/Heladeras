package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.incidente.TipoIncidenteEnum;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.IncidenteDTO;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStream;


public class CronService {

    private final HeladeraService heladeraService;
    private static final int INTERVAL_MINUTES = 1;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CronService() {
        this.heladeraService = new HeladeraService(); // Rompe un poquito DIP pero bueno
    }

    public void startJob(String endpointURL) {
        Runnable task = () -> {
            try {
                System.out.println("Consultando heladeras con fallas de conexión...");
                List<Heladera> heladerasDesconectadas = heladeraService.getHeladerasDesconectadas(INTERVAL_MINUTES);

                List<IncidenteDTO> incidentesDTODesconexion = heladerasDesconectadas.stream()
                        .map(heladera -> {
                            IncidenteDTO dto = new IncidenteDTO();
                            dto.setHeladeraId(heladera.getHeladeraId());
                            dto.setColaboradorId(0L);
                            dto.setDenunciante("CronJob");
                            dto.setTipoIncidente(TipoIncidenteEnum.CONEXION);
                            dto.setTimestamp(LocalDateTime.now());
                            return dto;
                        })
                        .toList();

                System.out.println("Consultando heladeras con problemas en temperatura...");
                List<Heladera> heladerasConProblemasDeTemperatura = heladeraService.getHeladerasTemperaturas(INTERVAL_MINUTES);

                List<IncidenteDTO> incidentesDTOTemperatura = heladerasConProblemasDeTemperatura.stream()
                        .map(heladera -> {
                            IncidenteDTO dto = new IncidenteDTO();
                            dto.setHeladeraId(heladera.getHeladeraId());
                            dto.setColaboradorId(0L);
                            dto.setDenunciante("CronJob");
                            dto.setTipoIncidente(TipoIncidenteEnum.TEMPERATURA);
                            dto.setTimestamp(LocalDateTime.now());
                            return dto;
                        })
                        .toList();

                List<IncidenteDTO> todosLosIncidentes = new java.util.ArrayList<>();
                todosLosIncidentes.addAll(incidentesDTODesconexion);
                todosLosIncidentes.addAll(incidentesDTOTemperatura);

                enviarIncidentes(endpointURL, todosLosIncidentes);

            } catch (Exception e) {
                System.err.println("Error durante la ejecución del job: " + e.getMessage());
            }
        };

        scheduler.scheduleAtFixedRate(task, 0, INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    private void enviarIncidentes(String endpointURL, List<IncidenteDTO> incidentesDTO) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(incidentesDTO);

            URL url = new URL(endpointURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Incidentes enviados exitosamente al endpoint.");
            } else {
                System.err.println("Error al enviar incidentes al endpoint. Código de respuesta: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            System.err.println("Error al enviar incidentes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
