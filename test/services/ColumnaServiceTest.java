import org.junit.*;

import static org.junit.Assert.*;

import play.db.jpa.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;

import java.io.Console;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import models.Tarea;
import services.TareaService;
import services.TareaServiceException;

import models.Columna;
import services.ColumnaService;
import services.ColumnaServiceException;

import models.Tablero;
import services.TableroService;
import services.TableroServiceException;

public class ColumnaServiceTest {
	static private Injector injector;

	@BeforeClass
	static public void initApplication() {
		GuiceApplicationBuilder guiceApplicationBuilder = new GuiceApplicationBuilder().in(Environment.simple());
		injector = guiceApplicationBuilder.injector();
		injector.instanceOf(JPAApi.class);
	}

	@Before
	public void initData() throws Exception {
		JndiDatabaseTester databaseTester = new JndiDatabaseTester("DBTest");
		IDataSet initialDataSet = new FlatXmlDataSetBuilder()
				.build(new FileInputStream("test/resources/usuarios_dataset.xml"));
		databaseTester.setDataSet(initialDataSet);
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		databaseTester.onSetup();
	}

	private TareaService newTareaService() {
		return injector.instanceOf(TareaService.class);
	}

	private TableroService newTableroService() {
		return injector.instanceOf(TableroService.class);
	}

	private ColumnaService newColumnaService() {
		return injector.instanceOf(ColumnaService.class);
	}

	@Test
	public void getColumnasTablero() {
		ColumnaService columnaService = newColumnaService();
		List<Columna> columnas = columnaService.allColumnasTablero(1000L);
		assertEquals(1, columnas.size());
	}

	@Test(expected = ColumnaServiceException.class)
	public void getColumnasTableroNoExiste() {
		ColumnaService columnaService = newColumnaService();
		List<Columna> columnas = columnaService.allColumnasTablero(5000L);
	}

	@Test
	public void crearColumnaTablero() {
		ColumnaService columnaService = newColumnaService();
		List<Columna> columnas = columnaService.allColumnasTablero(1001L);
		assertEquals(0, columnas.size());
		columnaService.nuevaColumna(1001L, "Testing");
		columnas = columnaService.allColumnasTablero(1001L);
		assertEquals(1, columnas.size());
	}

	@Test
	public void getTareasColumna() {
		ColumnaService columnaService = newColumnaService();
		List<Tarea> tareas = columnaService.allTareasColumna(1000L);
		assertEquals(0, tareas.size());
	}

	@Test
	public void setTareaColumna() {
		ColumnaService columnaService = newColumnaService();
		List<Tarea> tareas = columnaService.allTareasColumna(1000L);
		assertEquals(0, tareas.size());
		tareas = new ArrayList<>(columnaService.addTareaColumna(1000L, 1000L).getTareas());
		assertEquals(1, tareas.size());
	}

	@Test
	public void eliminarColumna(){
		ColumnaService columnaService = newColumnaService();
		Long idTablero = 1000L;
		Long idColumna = 1000L;
		List<Columna> columnas = columnaService.allColumnasTablero(idTablero);
		assertEquals(1,columnas.size());
		columnaService.borraColumna(idColumna);
		columnas = columnaService.allColumnasTablero(idTablero);
		assertEquals(0,columnas.size());
	}
}
