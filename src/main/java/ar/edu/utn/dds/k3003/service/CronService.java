package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.Heladera;
import ar.edu.utn.dds.k3003.model.incidente.TipoIncidenteEnum;
import ar.edu.utn.dds.k3003.presentation.auxiliar.DTOs.IncidenteDTO;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.OutputStream;


public class CronService {

    private final HeladeraService heladeraService;
    private static final int INTERVAL_MINUTES = 15;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final ObjectMapper objectMapper;

    public CronService(HeladeraService heladeraService) {
        this.heladeraService = heladeraService;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void startJob(String endpointURL) {
        Runnable task = () -> {
            System.out.println("########## HOLA SOY UN CRON Y VENGO A TRABAJAR ##########");
            try {
                System.out.println("Consultando heladeras con fallas de conexión...");
                List<Heladera> heladerasDesconectadas = heladeraService.getHeladerasDesconectadas(INTERVAL_MINUTES);

                if(heladerasDesconectadas.isEmpty()){
                    System.out.println(" > Que buena suerte ! No hay heladeras desconectadas a : "+LocalDateTime.now());
                }

                heladerasDesconectadas.forEach(heladera -> {
                    IncidenteDTO dto = new IncidenteDTO();
                    dto.setHeladeraId(heladera.getHeladeraId());
                    dto.setColaboradorId(0L);
                    dto.setDenunciante("CronJob");
                    dto.setTipoIncidente(TipoIncidenteEnum.CONEXION);
                    dto.setTimestamp(LocalDateTime.now());
                    enviarIncidente(endpointURL, dto);
                });

                System.out.println("Consultando heladeras con problemas en temperatura...");
                List<Heladera> heladerasConProblemasDeTemperatura = heladeraService.getHeladerasTemperaturas(INTERVAL_MINUTES);

                if(heladerasConProblemasDeTemperatura.isEmpty()){
                    System.out.println(" > Que buena suerte ! No hay heladeras con problemas de temperatura a : "+LocalDateTime.now());
                }

                heladerasConProblemasDeTemperatura.forEach(heladera -> {
                    IncidenteDTO dto = new IncidenteDTO();
                    dto.setHeladeraId(heladera.getHeladeraId());
                    dto.setColaboradorId(0L);
                    dto.setDenunciante("CronJob");
                    dto.setTipoIncidente(TipoIncidenteEnum.TEMPERATURA);
                    dto.setTimestamp(LocalDateTime.now());
                    enviarIncidente(endpointURL, dto);
                });

            } catch (Exception e) {
                System.err.println("Error durante la ejecución del job: " + e.getMessage());
            }

            System.out.println("########## YA TERMINE CAPO, NOS VIMOS ##########");

        };
        scheduler.scheduleWithFixedDelay(task, 0, INTERVAL_MINUTES, TimeUnit.MINUTES);
    }

    private void enviarIncidente(String endpointURL, IncidenteDTO incidenteDTO) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(incidenteDTO);

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
                System.out.println("Incidente enviado exitosamente: " + "Tipo de incidente : " + incidenteDTO.getTipoIncidente() + " | HeladeraId : " + incidenteDTO.getHeladeraId() + " | Timestamp : " + incidenteDTO.getTimestamp());
            } else {
                System.err.println("Error al enviar incidente. Código de respuesta: " + responseCode);
                try (InputStream is = connection.getErrorStream()) {
                    if (is != null) {
                        String responseMessage = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                        System.err.println("Mensaje de error del servidor: " + responseMessage);
                    }
                }
            }

            connection.disconnect();
        } catch (Exception e) {
            System.err.println("Error al enviar incidente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
