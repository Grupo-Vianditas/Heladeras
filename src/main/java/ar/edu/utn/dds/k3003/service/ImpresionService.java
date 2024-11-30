package ar.edu.utn.dds.k3003.service;


import ar.edu.utn.dds.k3003.model.Incidente;
import ar.edu.utn.dds.k3003.model.incidente.TipoIncidenteEnum;

public class ImpresionService {

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_BOLD = "\u001B[1m";


    public void imprimirIncidente(Incidente incidente) {
        if (incidente.getTipoIncidente() == TipoIncidenteEnum.FALLA_TECNICA) {
            imprimirFallaTecnica(incidente);
        } else {
            imprimirAlerta(incidente);
        }
    }

    private void imprimirAlerta(Incidente incidente) {
        String tipoColor = getColorPorTipo(incidente.getTipoIncidente());
        String subtipoColor = getColorPorSubtipo(incidente.getTipoIncidente());

        String mensaje = ANSI_BOLD + "\n--- INCIDENTE DETECTADO ---" + ANSI_RESET + "\n" +
                ANSI_BLUE + "Fecha: " + ANSI_RESET + incidente.getTimestamp() + "\n" +
                ANSI_GREEN + "Heladera ID: " + ANSI_RESET + incidente.getHeladeraId() + "\n" +
                "Tipo: " + tipoColor + incidente.getTipoIncidente() + ANSI_RESET + "\n" +
                "Subtipo: " + subtipoColor + getSubtipo(incidente) + ANSI_RESET + "\n";

        System.out.println(mensaje);
    }

    private void imprimirFallaTecnica(Incidente incidente) {
        String tipoColor = getColorPorTipo(incidente.getTipoIncidente());

        String mensaje = ANSI_BOLD + "\n--- FALLA TÉCNICA REPORTADA ---" + ANSI_RESET + "\n" +
                ANSI_BLUE + "Fecha: " + ANSI_RESET + incidente.getTimestamp() + "\n" +
                ANSI_GREEN + "Heladera ID: " + ANSI_RESET + incidente.getHeladeraId() + "\n" +
                "Tipo: " + tipoColor + incidente.getTipoIncidente() + ANSI_RESET + "\n";

        System.out.println(mensaje);
    }


    private String getColorPorTipo(TipoIncidenteEnum tipoIncidente) {
        return switch (tipoIncidente) {
            case TEMPERATURA -> ANSI_RED;
            case FRAUDE -> ANSI_PURPLE;
            case CONEXION -> ANSI_YELLOW;
            case FALLA_TECNICA -> ANSI_BLUE;
            case REPARACION -> ANSI_GREEN;
        };
    }

    private String getColorPorSubtipo(TipoIncidenteEnum tipoIncidente) {
        return switch (tipoIncidente) {
            case TEMPERATURA -> ANSI_RED;
            case FRAUDE -> ANSI_PURPLE;
            case CONEXION -> ANSI_YELLOW;
            default -> ANSI_RESET;
        };
    }


    private String getSubtipo(Incidente incidente) {
        return incidente.getTipoIncidente().name();
    }
}
