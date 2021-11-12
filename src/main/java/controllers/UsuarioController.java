package controllers;

import excepciones.ContraseniaInvalidaException;
import modelo.notificacion.TipoNotificadorPreferido;
import modelo.persona.DatosDeContacto;
import modelo.persona.DocumentoIdentidad;
import modelo.persona.Persona;
import modelo.persona.TipoDocumento;
import modelo.usuario.TipoUsuario;
import modelo.usuario.Usuario;
import modelo.usuario.ValidadorContrasenias;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import org.uqbarproject.jpa.java8.extras.transaction.TransactionalOps;
import repositorios.RepositorioUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UsuarioController extends Controller implements WithGlobalEntityManager, TransactionalOps {

  public ModelAndView mostrarAdmin(Request request, Response response) {
    if (noEsAdmin(request)) {
      response.redirect("/");
      return null;
    }
    return new ModelAndView(getMap(request), "admin.html.hbs");
  }

  public ModelAndView mostrarFormularioCreacionUsuario(Request request, Response response) {
    return new ModelAndView(getMap(request), "registracion.html.hbs");
  }

  public Void registrarUsuario(Request request, Response response) {

    String contrasenia = request.queryParams("contrasenia");
    String validacionContrasenia = request.queryParams("validacionContrasenia");

    try {
      new ValidadorContrasenias().correrValidaciones(request.queryParams("contrasenia"));
    } catch (ContraseniaInvalidaException e) {
      redireccionCasoError(request, response, "/creacion-usuario", e.getMessage());
      return null;
    }

    if(!contrasenia.equals(validacionContrasenia)){
      redireccionCasoError(request, response, "/creacion-usuario", "Las contraseñas no matchean entre si");
      return null;
    }

    if (new RepositorioUsuarios().yaExiste(request.queryParams("usuario"))) {
      redireccionCasoError(request, response,"/creacion-usuario", "Ya existe una cuenta con el nombre de usuario ingresado");
    } else {
      DocumentoIdentidad documentoIdentidad = new DocumentoIdentidad(
          TipoDocumento.values()[Integer.parseInt(request.queryParams("tipoDocumento"))],
          request.queryParams("numeroDocumento")
      );

      DatosDeContacto datosDeContacto = new DatosDeContacto(
          request.queryParams("telefono"),
          request.queryParams("email")
      );

      Persona persona = new Persona(
          request.queryParams("nombre"),
          request.queryParams("apellido"),
          documentoIdentidad,
          datosDeContacto,
          LocalDate.parse(request.queryParams("fechaNacimiento"), DateTimeFormatter.ofPattern("MM/dd/yyyy")),
          TipoNotificadorPreferido.values()[Integer.parseInt(request.queryParams("tipoNotificadorPreferido"))]
      );

      Usuario nuevo = new Usuario(
          request.queryParams("usuario"),
          request.queryParams("contrasenia"),
          TipoUsuario.NORMAL,
          persona
      );

      withTransaction(() -> {
        new RepositorioUsuarios().agregar(nuevo);
      });

      request.session().attribute("user_id", nuevo.getId());
      redireccionCasoFeliz(request, response, "/", "La cuenta se ha registrado con exito!");
    }
    return null;
  }
}