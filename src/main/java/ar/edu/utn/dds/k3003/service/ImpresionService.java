package ar.edu.utn.dds.k3003.service;

import ar.edu.utn.dds.k3003.model.incidentes.Incidente;
import ar.edu.utn.dds.k3003.model.incidentes.IncidenteBase;
import ar.edu.utn.dds.k3003.model.incidentes.concretos.IncidenteAlerta;
import ar.edu.utn.dds.k3003.model.incidentes.subtipos.SubtipoAlerta;
import org.jetbrains.annotations.NotNull;

public class ImpresionService {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_BOLD = "\u001B[1m";

    public void imprimirIncidente(Incidente incidente) {
        if (incidente instanceof IncidenteAlerta incidenteAlerta) {
            String tipoColor = getColorPorTipo(incidenteAlerta.getTipoIncidente());
            String tipoIncidente = tipoColor + incidenteAlerta.getTipoIncidente() + ANSI_RESET;
            String subtipoColor = getColorPorSubtipo(incidenteAlerta.getSubtipoAlerta());

            String mensaje = ANSI_BOLD + "\n--- INCIDENTE DETECTADO ---" + ANSI_RESET + "\n" +
                    ANSI_BLUE + "Fecha: " + ANSI_RESET + incidenteAlerta.getFechaIncidente() + "\n" +
                    ANSI_GREEN + "Heladera ID: " + ANSI_RESET + incidenteAlerta.getHeladeraId() + "\n" +
                    "Tipo: " + tipoIncidente + "\n" +
                    "Subtipo: " + subtipoColor + incidenteAlerta.getSubtipoAlerta() + ANSI_RESET + "\n";

            System.out.println(mensaje);
        } else if (incidente instanceof IncidenteBase incidenteBase) {
            String mensaje = getString(incidenteBase);

            System.out.println(mensaje);
        } else {
            System.out.println(ANSI_RED + "Incidente no soportado para impresión." + ANSI_RESET);
        }
    }

    private @NotNull String getString(IncidenteBase incidenteBase) {
        String tipoColor = getColorPorTipo(incidenteBase.getTipoIncidente());
        String tipoIncidente = tipoColor + incidenteBase.getTipoIncidente() + ANSI_RESET;

        String mensaje = ANSI_BOLD + "\n--- INCIDENTE DETECTADO ---" + ANSI_RESET + "\n" +
                ANSI_BLUE + "Fecha: " + ANSI_RESET + incidenteBase.getFechaIncidente() + "\n" +
                ANSI_GREEN + "Heladera ID: " + ANSI_RESET + incidenteBase.getHeladeraId() + "\n" +
                "Tipo: " + tipoIncidente + "\n";
        return mensaje;
    }

    private String getColorPorTipo(String tipoIncidente) {
        return switch (tipoIncidente) {
            case "ALERTA" -> ANSI_YELLOW;
            case "TECNICO" -> ANSI_RED;
            default -> ANSI_BLUE;
        };
    }

    private String getColorPorSubtipo(SubtipoAlerta subtipoAlerta) {
        return switch (subtipoAlerta) {
            case TEMPERATURA_FUERA_DE_RANGO -> ANSI_RED;
            case FRAUDE_DE_MOVIMIENTO -> ANSI_PURPLE;
            case FALLA_DE_CONEXION -> ANSI_YELLOW;
            default -> ANSI_RESET;
        };
    }
}
