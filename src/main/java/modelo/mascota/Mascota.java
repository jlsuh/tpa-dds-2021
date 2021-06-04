package modelo.mascota;

import modelo.mascota.caracteristica.Caracteristica;

import java.time.LocalDate;
import java.util.List;

public class Mascota {

  private Animal animal;
  private String nombre;
  private String apodo;
  private LocalDate fechaNacimiento;
  private Sexo sexo;
  private String descripcionFisica;
  private List<Caracteristica> caracteristicas;
  private List<Foto> fotos;

  public Mascota(Animal animal, String nombre, String apodo, LocalDate fechaNacimiento, Sexo sexo,
      String descripcionFisica, List<Caracteristica> catacteristicas, List<Foto> fotos) {
    this.animal = animal;
    this.nombre = nombre;
    this.apodo = apodo;
    this.fechaNacimiento = fechaNacimiento;
    this.sexo = sexo;
    this.descripcionFisica = descripcionFisica;
    this.caracteristicas = catacteristicas;
    this.fotos = fotos;
  }

  public Animal getAnimal() {
    return animal;
  }

  public String getNombre() {
    return nombre;
  }

  public String getApodo() {
    return apodo;
  }

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public Sexo getSexo() {
    return sexo;
  }

  public String getDescripcionFisica() {
    return descripcionFisica;
  }

  public List<Foto> getFotos() {
    return fotos;
  }

  public List<Caracteristica> getCaracteristicas() {
    return caracteristicas;
  }

}