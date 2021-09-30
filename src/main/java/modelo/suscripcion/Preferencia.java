package modelo.suscripcion;

import java.util.List;

import modelo.mascota.Animal;
import modelo.mascota.caracteristica.Caracteristica;

import javax.persistence.Embeddable;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;

@Embeddable
public class Preferencia {

  @ManyToMany
  private List<Caracteristica> caracteristicas;
  @Enumerated
  private Animal tipoAnimal;

  public Preferencia(List<Caracteristica> caracteristicas, Animal tipoAnimal) {
    this.caracteristicas = caracteristicas;
    this.tipoAnimal = tipoAnimal;
  }

  public List<Caracteristica> getCaracteristicas() {
    return this.caracteristicas;
  }

  public Animal getTipoAnimal() {
    return this.tipoAnimal;
  }
}
