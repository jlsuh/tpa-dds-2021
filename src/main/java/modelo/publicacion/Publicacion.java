package modelo.publicacion;

import modelo.notificacion.Notificacion;
import modelo.notificacion.Notificador;
import modelo.persona.DatosDeContacto;
import modelo.usuario.Usuario;
import repositorios.RepositorioPublicaciones;

public abstract class Publicacion {

  private DatosDeContacto contactoPosteador;
  private Notificador notificador;
  private RepositorioPublicaciones repositorioPublicaciones;
  // private Asociacion asociacion; // TODO: Integrar en futuras ocasiones

  public Publicacion(DatosDeContacto contactoPosteador, Notificador notificador, RepositorioPublicaciones repositorioPublicaciones) {
    // TODO: Hace falta validación? o confiar en lo de adentro?
    this.contactoPosteador = contactoPosteador;
    this.notificador = notificador;
    this.repositorioPublicaciones = repositorioPublicaciones;
  }

  /**
   * Marca la publicacion como publicacionProcesada y usa un Notificador
   */
  public void lograrObjetivoDeLaPublicacion(Usuario usuario) {
    this.repositorioPublicaciones.marcarPublicacionComoProcesada(this);
    this.notificarPosteador(usuario);
  }

  /**
   * @see DarEnAdopcion::generarNotificacion/1
   */
  public void notificarPosteador(Usuario usuario) {
    Notificacion notificacion = this.generarNotificacion(usuario);
    if (notificacion != null) { // TODO: ¿Instancias de DarEnAdopcion también envían notificación?
                                // En caso de que así sea, quitar este if y modelar la Notificación
                                // a ser enviada para DarEnAdocion
      this.notificador.notificar(notificacion);
    }
  }

  public abstract Notificacion generarNotificacion(Usuario usuario);

  public DatosDeContacto getContactoPosteador() {
    return this.contactoPosteador;
  }

  public Notificador getNotificador() {
    return this.notificador;
  }

}
