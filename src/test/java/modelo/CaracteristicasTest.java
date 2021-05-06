package modelo;

import excepciones.CaracteristicasVacioException;
import excepciones.ValorCaracteristicaIncompatibleException;
import org.junit.jupiter.api.*;
import servicios.repositorios.RepositorioCaracteristicas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CaracteristicasTest {
	private RepositorioCaracteristicas respositorioCaracteristicas= RepositorioCaracteristicas.getInstance();
	private static Mascota beto;
	private static DuenioMascota pablo;
	private static Persona pabloPersona;
	private static DatosDeContacto datosPablo;
	private static DatosDeContacto datosAdmin;
	private static Persona adminPersona;
	private static Administrador admin;

	@BeforeAll
	public static void contextTest(){

		datosAdmin = new DatosDeContacto(null, "admin@mail.com");
		adminPersona = new Persona("admin3", "admin", TipoDocumento.DNI, "1000001", datosAdmin, LocalDate.of(1995, 8, 7) );
		admin = new Administrador("eladmin3","ElC4p0.123",adminPersona);

	}

	@Test
	public void unAdministradorPuedeAgregarCaracteristicas(){
		List<String> valoresPosible = Arrays.asList("SI", "NO");
		Caracteristica caracteristica = new Caracteristica("Castrado",valoresPosible);
		admin.agregarCaracteristica(caracteristica);
		Assertions.assertTrue(respositorioCaracteristicas.getListaCaracteristicas().contains(caracteristica));
	}

	@Test
	public void unMascotaTieneCaracteristicaInvalida(){
		Caracteristica caracteristica= new Caracteristica("Dormilon", Collections.singletonList("SI"));
		Mascota mascota=crearMascota(caracteristica);
		Assertions.assertThrows(CaracteristicasVacioException.class,()->duenioMascota.agregarMascota(mascota));
	}

	@Test
	@DisplayName("Persona asigna un valor invalido a una caracterstica previamente cargada lanza una exception")
	public void personaAsignaValorInvalidoACaractersticaGeneraUnaException(){
		Caracteristica caracteristica= new Caracteristica("Castrado", Collections.singletonList("Maso"));
		List<Caracteristica> listaCaracteristica=new ArrayList<>();
		listaCaracteristica.add(caracteristica);
		beto = new Mascota(Animal.PERRO, "beto", "jp", LocalDate.of(2018,3, 4), Sexo.MACHO, "mancito", listaCaracteristica, null );

		Assertions.assertThrows(ValorCaracteristicaIncompatibleException.class,()->pablo.agregarMascota(beto));
	}
}
