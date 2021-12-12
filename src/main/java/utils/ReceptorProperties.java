package utils;

import excepciones.RepositorioPropertiesException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class ReceptorProperties {

  private final Properties properties = new Properties();

  public ReceptorProperties() {
    cargarPath( getRuta(null) );
  }

  // para tests
  public ReceptorProperties(String fileName) {
    cargarPath( getRuta(fileName) );
  }

  private void cargarPath(String ruta) {
    try ( InputStream stream = new FileInputStream(ruta) ) {
      properties.load(stream);

    } catch (IOException e) {
      throw new RepositorioPropertiesException(e.toString());
    }
  }

  public String getRuta(String nombre){
    if (nombre == null) {
      // TODO: si esto no funciona, se puede probar lo que esta en clase "GestorArchivos" en metodo "abrirArchivo()"
      URL url = getClass().getClassLoader().getResource( Constantes.getPropertiesFileName() );
      if (url == null) throw new RepositorioPropertiesException("No existe el file");
      return url.getPath();

    } else {
      return Constantes.getDirectoryPath(nombre);
    }
  }

  public Properties getProperties() {
    return properties;
  }

}
