package modelo.persona;

public class DatosDeContacto {
  private String telefono;
  private String email;

  public DatosDeContacto(String telefono, String email) {
    this.telefono = telefono;
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public Boolean hayAlgunDatoDeContacto() {
    return (this.telefono != null || this.email != null);
  }
  
  public boolean existeCorreoAsociado() {
    return this.email != null;
  }

}