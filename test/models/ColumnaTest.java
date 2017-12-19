import org.junit.*;
import static org.junit.Assert.*;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import play.Logger;

import java.sql.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import java.util.List;
import java.util.HashSet;

import models.Usuario;
import models.UsuarioRepository;
import models.JPAUsuarioRepository;
import models.Tablero;
import models.TableroRepository;
import models.JPATableroRepository;
import models.Columna;
import models.ColumnaRepository;
import models.JPAColumnaRepository;
import models.Tarea;
import models.TareaRepository;
import models.JPATareaRepository;

public class ColumnaTest {
	static Database db;
	static private Injector injector;

	@BeforeClass
	static public void initApplication() {
		GuiceApplicationBuilder guiceApplicationBuilder = new GuiceApplicationBuilder().in(Environment.simple());
		injector = guiceApplicationBuilder.injector();
		db = injector.instanceOf(Database.class);
		injector.instanceOf(JPAApi.class);
	}

	@Before
	public void initData() throws Exception {
		JndiDatabaseTester databaseTester = new JndiDatabaseTester("DBTest");
		IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("test/resources/usuarios_dataset.xml"));
		databaseTester.setDataSet(initialDataSet);
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		databaseTester.onSetup();
	}

	private TareaRepository newTareaRepository() {
		return injector.instanceOf(TareaRepository.class);
	}

	private UsuarioRepository newUsuarioRepository() {
		return injector.instanceOf(UsuarioRepository.class);
	}

	private TableroRepository newTableroRepository() {
		return injector.instanceOf(TableroRepository.class);
	}

	private ColumnaRepository newColumnaRepository() {
		return injector.instanceOf(ColumnaRepository.class);
	}

	@Test
	public void testCrearColumna() {
		Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
		Tablero t = new Tablero(usuario, "MADS-ToDoList");
		Columna col = new Columna(t, "Sprint");

		assertEquals("Sprint", col.getNombre());
		assertEquals(new HashSet<Columna>(), col.getTareas());
		assertEquals("MADS-ToDoList", col.getTablero().getNombre());
	}

	public void testAssertEquals() {
		Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
		Tablero t = new Tablero(usuario, "MADS-ToDoList");
		Columna col = new Columna(t, "Sprint");
		Columna col2 = new Columna(t, "Sprint");
		assertEquals(col, col2);
	}

	public void testAddColumnaRepository() {
		UsuarioRepository usuarioRepository = newUsuarioRepository();
		Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
		usuario = usuarioRepository.add(usuario);
		TableroRepository tableroRepository = newTableroRepository();
		Tablero tablero = new Tablero(usuario, "MADS-ToDoList");
		tablero = tableroRepository.add(tablero);
		assertNotNull(tablero.getId());
	}

	public void testUpdateColumnaRepository() {
		UsuarioRepository usuarioRepository = newUsuarioRepository();
		Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
		usuario = usuarioRepository.add(usuario);
		TableroRepository tableroRepository = newTableroRepository();
		Tablero tablero = new Tablero(usuario, "MADS-ToDoList");
		tablero = tableroRepository.add(tablero);
		tablero.setNombre("Mads-ListaTareas");
		tablero = tableroRepository.update(tablero);
		assertNotNull(tablero.getId());
		assertEquals("Mads-ListaTareas", tablero.getNombre());
	}
}
