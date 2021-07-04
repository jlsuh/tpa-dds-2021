package entregaTPA3;

import modelo.adopcion.RecomendadorDeAdopciones;
import modelo.asociacion.Asociacion;
import modelo.mascota.Animal;
import modelo.notificacion.NotificadorCorreo;
import modelo.pregunta.RespuestaDelAdoptante;
import modelo.pregunta.RespuestaDelDador;
import modelo.suscripcion.Preferencia;
import modelo.suscripcion.SuscripcionParaAdopcion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repositorios.RepositorioDarEnAdopcion;
import repositorios.RepositorioSuscripcionesParaAdopciones;
import utils.DummyData;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class Punto5 {

  NotificadorCorreo notificadorCorreo;
  RecomendadorDeAdopciones recomendador;
  RepositorioSuscripcionesParaAdopciones repositorioSuscripcionesParaAdopciones = new RepositorioSuscripcionesParaAdopciones();
  RepositorioDarEnAdopcion repositorioDarEnAdopcion = new RepositorioDarEnAdopcion();
  RespuestaDelAdoptante respuestaAdoptante1 = new RespuestaDelAdoptante("Si", DummyData.getParDePreguntas1());
  RespuestaDelAdoptante respuestaAdoptante2 = new RespuestaDelAdoptante("3", DummyData.getParDePreguntas2());
  SuscripcionParaAdopcion suscriptionSpy;

  @BeforeEach
  public void contextLoad() {
    notificadorCorreo = mock(NotificadorCorreo.class);
    SuscripcionParaAdopcion suscripcionParaAdopcion;
    suscripcionParaAdopcion = new SuscripcionParaAdopcion(DummyData.getDatosDeContacto(notificadorCorreo),
        new Asociacion(DummyData.getUbicacion()), new Preferencia(DummyData.getCaracteristicasParaMascota(),
        Animal.PERRO), Arrays.asList(respuestaAdoptante1, respuestaAdoptante2));
    suscriptionSpy = Mockito.spy(suscripcionParaAdopcion);
    repositorioSuscripcionesParaAdopciones.agregar(suscriptionSpy);
    recomendador = new RecomendadorDeAdopciones(5, repositorioDarEnAdopcion, repositorioSuscripcionesParaAdopciones);
  }

  @Test
  public void seEnviaRecomendacionCuandoElRecomendadorRecomienda() {
    recomendador.recomendarAdopcionesASuscritos();
    verify(suscriptionSpy).enviarRecomendaciones(any());
  }

}
