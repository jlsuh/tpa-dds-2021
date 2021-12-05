package controllers;

import modelo.asociacion.Asociacion;
import modelo.pregunta.ParDePreguntas;
import modelo.pregunta.ParDeRespuestas;
import repositorios.RepositorioAsociaciones;
import repositorios.RepositorioParDePreguntas;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PreguntasController extends Controller {

  private RepositorioAsociaciones repositorioAsociaciones = new RepositorioAsociaciones();
  private RepositorioParDePreguntas repositorioParDePreguntas = new RepositorioParDePreguntas();
  private final int totalRespuestasPosibles = 5;

  public ModelAndView mostrarPreguntasDeLaAsociacion(Request request, Response response) {
    String idAsociacion = request.params(":idAsociacion");
    List<ParDePreguntas> paresDePreguntas;
    Map<String, Object> modelo = getMap(request);

    if(idAsociacion.equals("0")) {
      request.session().attribute("es_obligatoria", true);
      paresDePreguntas = repositorioParDePreguntas.getPreguntasObligatorias();
    } else {
      request.session().attribute("es_obligatoria", false);
      Asociacion asociacionBuscada = repositorioAsociaciones.buscarPorId(Long.parseLong(idAsociacion));
      paresDePreguntas = asociacionBuscada.getPreguntas();
      modelo.put("asociacion", asociacionBuscada);
    }

    modelo.put("asociaciones", repositorioAsociaciones.listarTodos());
    modelo.put("preguntas", paresDePreguntas);
    modelo.put("esObligatoria", request.session().attribute("es_obligatoria"));
    modelo.put("mostrarBotonAgregarPreguntas", true);
    return new ModelAndView(modelo, "preguntas-asociaciones.html.hbs");
  }

  public ModelAndView nuevaPregunta(Request request, Response response) {
    String idAsociacion = request.params(":idAsociacion");
    Map<String, Object> modelo = getMap(request);
    modelo.put("asociacion", idAsociacion);
    modelo.put("rangoDeRespuestas", super.obtenerRango(totalRespuestasPosibles));
    return new ModelAndView(modelo, "nueva-pregunta.html.hbs");
  }

  public ModelAndView matchearRespuestasPosibles(Request request, Response response) {
    
    List<String> respuestasPosiblesDelDador = new ArrayList<>();
    List<String> respuestasPosiblesDelAdoptante = new ArrayList<>();

    super.obtenerRango(totalRespuestasPosibles).forEach(i -> {
      respuestasPosiblesDelDador.add(request.queryParams("respuestaPosibleDador".concat(String.valueOf(i))));
      respuestasPosiblesDelAdoptante.add(request.queryParams("respuestaPosibleAdoptante".concat(String.valueOf(i))));
    });

    respuestasPosiblesDelDador.removeAll(Collections.singleton(""));
    respuestasPosiblesDelAdoptante.removeAll(Collections.singleton(""));

    if(respuestasPosiblesDelDador.size() <= 1 || respuestasPosiblesDelAdoptante.size() <= 1) {
      super.redireccionCasoError(request, response, null, "Debe ingresar mas de una respuesta posible");
    }

    BorradorParDePreguntas borradorParDePreguntas = new BorradorParDePreguntas(
        Long.parseLong(request.params(":idAsociacion")),
        request.queryParams("concepto"),
        request.queryParams("preguntaDador"),
        request.queryParams("preguntaAdoptante"),
        request.session().attribute("es_obligatoria"),
        respuestasPosiblesDelDador,
        respuestasPosiblesDelAdoptante
    );

    Map<String, Object> modelo = getMap(request);
    if(!borradorParDePreguntas.getEsPreguntaObligatoria()) {
      modelo.put("asociacion", repositorioAsociaciones.buscarPorId(borradorParDePreguntas.getAsociacionId()));
    }
    modelo.put("cantidadRespuestasPosibles", super.obtenerRango(totalRespuestasPosibles));
    modelo.put("respuestasPosiblesDador", borradorParDePreguntas.getRespuestasPosiblesDelDador());
    modelo.put("respuestasPosiblesAdoptante", borradorParDePreguntas.getRespuestasPosiblesDelAdoptante());
    modelo.put("asociacionId", borradorParDePreguntas.getAsociacionId());
    modelo.put("preguntaDador", borradorParDePreguntas.getPreguntaDelDador());
    modelo.put("preguntaAdoptante", borradorParDePreguntas.getPreguntaDelAdoptante());

    request.session().attribute("borrador_par_preguntas", borradorParDePreguntas);
    return new ModelAndView(modelo, "nueva-pregunta-2.html.hbs");
  }

  public Void crearParDePreguntasAsociacion(Request request, Response response) {

    BorradorParDePreguntas borradorParDePreguntas = request.session().attribute("borrador_par_preguntas");
    List<ParDeRespuestas> paresDeRespuestas = new ArrayList<>();

    super.obtenerRango(totalRespuestasPosibles).forEach(i -> {
      paresDeRespuestas.add(
          new ParDeRespuestas(
              request.queryParams("respuestaPosibleDador".concat(String.valueOf(i))),
              request.queryParams("respuestaPosibleAdoptante".concat(String.valueOf(i)))
          )
      );
    });

    List<ParDeRespuestas> paresDeRespuestasFiltradas =
        paresDeRespuestas.stream().filter(
            par -> !par.getRespuestaDelDador().equals("Elegir respuesta posible dador...")
                && !par.getRespuestaDelAdoptante().equals("Elegir respuesta posible adoptante...")
        ).collect(Collectors.toList());

    ParDePreguntas parDePreguntas = new ParDePreguntas(
        borradorParDePreguntas.getConcepto(),
        borradorParDePreguntas.getPreguntaDelDador(),
        borradorParDePreguntas.getPreguntaDelAdoptante(),
        borradorParDePreguntas.getEsPreguntaObligatoria(),
        borradorParDePreguntas.getRespuestasPosiblesDelDador(),
        borradorParDePreguntas.getRespuestasPosiblesDelAdoptante(),
        paresDeRespuestasFiltradas
    );

    withTransaction(() -> {
      repositorioParDePreguntas.agregar(parDePreguntas);
      if(!borradorParDePreguntas.getEsPreguntaObligatoria()) {
        Asociacion asociacion = repositorioAsociaciones.buscarPorId(borradorParDePreguntas.getAsociacionId());
        asociacion.agregarPregunta(parDePreguntas);
      }
    });

    response.redirect("/asociaciones/".concat((String.valueOf(borradorParDePreguntas.getAsociacionId())).concat("/preguntas")));
    request.session().removeAttribute("borrador_par_preguntas");
    request.session().removeAttribute("es_obligatoria");
    return null;
  }

}
