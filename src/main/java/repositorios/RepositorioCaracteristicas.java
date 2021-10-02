package repositorios;

import modelo.mascota.caracteristica.Caracteristica;
import modelo.mascota.caracteristica.CaracteristicaConValoresPosibles;
import org.uqbarproject.jpa.java8.extras.WithGlobalEntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioCaracteristicas implements WithGlobalEntityManager {

  public List<Caracteristica> getCaracteristicas() {
    return entityManager()
        .createQuery("from Caracteristica", Caracteristica.class)
        .getResultList();
  }

  public void agregarCaracteristicasConValoresPosibles(CaracteristicaConValoresPosibles caracteristica) {
    caracteristica.listarCaracteristicas().forEach(c -> entityManager().persist(c));
  }

  public void eliminarCaracteristicasConValoresPosibles(CaracteristicaConValoresPosibles caracteristica) {
    entityManager()
        .createQuery("DELETE FROM Caracteristica c WHERE c.nombreCaracteristica = :nombre")
        .setParameter("nombre", caracteristica.getNombreCaracteristica())
        .executeUpdate();
  }

  public List<CaracteristicaConValoresPosibles> getCaracteristicasConValoresPosibles() {
    List<CaracteristicaConValoresPosibles> lista = new ArrayList<>();

    entityManager()
        .createQuery("from Caracteristica", Caracteristica.class)
        .getResultList()
        .stream()
        .collect(Collectors.groupingBy(Caracteristica::getNombreCaracteristica))
        .forEach((n, c) -> lista.add(
            new CaracteristicaConValoresPosibles(
                n, c.stream().map(Caracteristica::getValorCaracteristica).collect(Collectors.toList())
            )
        ));

    return lista;
  }
}