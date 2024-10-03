package ar.edu.utn.dds.k3003.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.InjectMocks;

@ExtendWith(MockitoExtension.class)
public class FachadaTest {

    private static final Integer HELADERA_ID = 376518;
    private static final String NOMBRE_HELADERA = "Heladera de prueba";
    private static final String qr1 = "Vale por una vianda";

    @InjectMocks
    Fachada instancia;

    @Mock
    FachadaViandas viandas;

    @BeforeEach
    void setUp() {
        instancia.setViandasProxy(viandas);
    }

    @Test
    @DisplayName("Agregar heladeras")
    void agregarHeladeras(){
        instancia.agregar(new HeladeraDTO(HELADERA_ID, NOMBRE_HELADERA,0));
        instancia.agregar(new HeladeraDTO(123, "Heladera falsa",0));
        instancia.agregar(new HeladeraDTO(234, "Hola, soy una heladera",0));
        instancia.agregar(new HeladeraDTO(null, "Pedro",0));

        assertEquals(4, instancia.getHeladeraRepository().cantidadDeHeladeras(), "No se agregaron correctamente las 4 heladeras");
    }

    @Test
    @DisplayName("Agregar heladeras con el mismo Id")
    void agregarHeladeraConMismoId(){
        instancia.agregar(new HeladeraDTO(HELADERA_ID, NOMBRE_HELADERA,0));

        assertEquals(1, instancia.getHeladeraRepository().cantidadDeHeladeras(), "Se agregó una heladera correctamente");

        assertThrows(IllegalArgumentException.class, () -> instancia.agregar(new HeladeraDTO(HELADERA_ID, "Soy una mentira",0)), "No se lanzó la excepción esperada");
    }

    @Test
    @DisplayName("Depositar vianda válida en una heladera válida")
    void testDepositarVianda() {
        instancia.agregar(new HeladeraDTO(HELADERA_ID, NOMBRE_HELADERA,0));

        when(viandas.buscarXQR(qr1)).thenReturn(new ViandaDTO(qr1, LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 14L, HELADERA_ID));

        instancia.depositar(HELADERA_ID, qr1);

        verify(viandas).modificarEstado(qr1, EstadoViandaEnum.DEPOSITADA);

        assertEquals(1, instancia.cantidadViandas(HELADERA_ID), "La vianda no se agregó correctamente");
    }

    @Test
    @DisplayName("Retirar viandas")
    void testRetirarViandas() {
        String qr1 = "Vale por una empanada";
        String qr2 = "Vale por un sánguche";
        String qr3 = "Vale por una milanesa";

        when(viandas.buscarXQR(qr1)).thenReturn(new ViandaDTO(qr1, LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 15L, HELADERA_ID));
        when(viandas.buscarXQR(qr2)).thenReturn(new ViandaDTO(qr2, LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 15L, HELADERA_ID));
        when(viandas.buscarXQR(qr3)).thenReturn(new ViandaDTO(qr3, LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 15L, HELADERA_ID));

        instancia.agregar(new HeladeraDTO(HELADERA_ID, NOMBRE_HELADERA,0));
        instancia.depositar(HELADERA_ID, qr1);
        instancia.depositar(HELADERA_ID, qr2);
        instancia.depositar(HELADERA_ID, qr3);

        instancia.retirar(new RetiroDTO(qr1, "14L", HELADERA_ID));
        instancia.retirar(new RetiroDTO(qr2, "14L", HELADERA_ID));
        instancia.retirar(new RetiroDTO(qr3, "14L", HELADERA_ID));

        verify(viandas, times(3)).modificarEstado(anyString(), eq(EstadoViandaEnum.RETIRADA));

        assertEquals(0, instancia.cantidadViandas(HELADERA_ID), "Las viandas no se retiraron correctamente");
    }

    @Test
    @DisplayName("Guardar temperaturas")
    void testGuardarTemperaturas() {
        instancia.agregar(new HeladeraDTO(HELADERA_ID, NOMBRE_HELADERA,0));

        instancia.temperatura(new TemperaturaDTO(64, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(89, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(-5, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(-10, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(0, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(17, HELADERA_ID, LocalDateTime.now()));

        assertEquals(
                6,
                instancia.obtenerTemperaturas(HELADERA_ID).size(),
                "Las temperaturas de una heladera no se guardan/recuperan correctamente");
    }

    @Test
    @DisplayName("Obtener temperaturas")
    void testObtenerTemperaturas() {
        instancia.agregar(new HeladeraDTO(HELADERA_ID, NOMBRE_HELADERA,0));

        instancia.temperatura(new TemperaturaDTO(64, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(89, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(-5, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(-10, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(0, HELADERA_ID, LocalDateTime.now()));
        instancia.temperatura(new TemperaturaDTO(17, HELADERA_ID, LocalDateTime.now()));

        List<TemperaturaDTO> temperaturaDTOS = instancia.obtenerTemperaturas(HELADERA_ID);
        assertEquals(
                17,
                temperaturaDTOS.get(0).getTemperatura(),
                "Las temperaturas de una heladera no se guardan/recuperan correctamente");
    }

    @Test
    @DisplayName("Depositar viandas no preparadas")
    void testDepositarViandasNoValidas() {
        instancia.agregar(new HeladeraDTO(HELADERA_ID, NOMBRE_HELADERA,0));

        long colaboradorId = 14L;

        String qr2 = "Vale por una vianda vencida";
        String qr3 = "Vale por una vianda retirada";
        String qr4 = "Vale por una vianda en traslado";
        String qr5 = "Vale por una vianda depositada";

        when(viandas.buscarXQR(qr2)).thenReturn(new ViandaDTO(qr2, LocalDateTime.now(), EstadoViandaEnum.VENCIDA, colaboradorId, HELADERA_ID));
        when(viandas.buscarXQR(qr3)).thenReturn(new ViandaDTO(qr3, LocalDateTime.now(), EstadoViandaEnum.RETIRADA, colaboradorId, HELADERA_ID));
        when(viandas.buscarXQR(qr4)).thenReturn(new ViandaDTO(qr4, LocalDateTime.now(), EstadoViandaEnum.EN_TRASLADO, colaboradorId, HELADERA_ID));
        when(viandas.buscarXQR(qr5)).thenReturn(new ViandaDTO(qr5, LocalDateTime.now(), EstadoViandaEnum.DEPOSITADA, colaboradorId, HELADERA_ID));

        instancia.depositar(HELADERA_ID, qr2);
        instancia.depositar(HELADERA_ID, qr3);
        instancia.depositar(HELADERA_ID, qr4);
        instancia.depositar(HELADERA_ID, qr5);

        assertEquals(4, instancia.cantidadViandas(HELADERA_ID), "Alguna vianda no se cargó en la heladera...");
    }

    @Test
    @DisplayName("Validar cantidad de viandas en heladeras distintas")
    void testCantidadViandasEnHeladerasDistintas() {
        Integer segundaHeladeraId = 8934;
        String qrViandaEnHeladeraPrincipal = "Dame";
        String qrOtraViandaEnHeladeraPrincipal = "Una";
        String qrTerceraViandaEnHeladeraPrincipal = "Viandita";
        String qrViandaEnSegundaHeladera = "Porfa";
        long colaboradorId = 14L;

        when(viandas.buscarXQR(qrViandaEnHeladeraPrincipal)).thenReturn(new ViandaDTO(qrViandaEnHeladeraPrincipal, LocalDateTime.now(), EstadoViandaEnum.PREPARADA, colaboradorId, HELADERA_ID));
        when(viandas.buscarXQR(qrOtraViandaEnHeladeraPrincipal)).thenReturn(new ViandaDTO(qrOtraViandaEnHeladeraPrincipal, LocalDateTime.now(), EstadoViandaEnum.PREPARADA, colaboradorId, HELADERA_ID));
        when(viandas.buscarXQR(qrTerceraViandaEnHeladeraPrincipal)).thenReturn(new ViandaDTO(qrTerceraViandaEnHeladeraPrincipal, LocalDateTime.now(), EstadoViandaEnum.PREPARADA, colaboradorId, HELADERA_ID));
        when(viandas.buscarXQR(qrViandaEnSegundaHeladera)).thenReturn(new ViandaDTO(qrViandaEnSegundaHeladera, LocalDateTime.now(), EstadoViandaEnum.PREPARADA, colaboradorId, segundaHeladeraId));

        instancia.agregar(new HeladeraDTO(HELADERA_ID, NOMBRE_HELADERA,0));
        instancia.agregar(new HeladeraDTO(segundaHeladeraId, "Soy una heladera",0));
        instancia.depositar(HELADERA_ID, qrViandaEnHeladeraPrincipal);
        instancia.depositar(HELADERA_ID, qrOtraViandaEnHeladeraPrincipal);
        instancia.depositar(HELADERA_ID, qrTerceraViandaEnHeladeraPrincipal);
        instancia.depositar(segundaHeladeraId, qrViandaEnSegundaHeladera);

        assertEquals(3, instancia.cantidadViandas(HELADERA_ID), "No se agregaron correctamente las 3 viandas a la heladera principal");
        assertEquals(1, instancia.cantidadViandas(segundaHeladeraId), "No se agregó correctamente la vianda a la segunda heladera");
    }

}
