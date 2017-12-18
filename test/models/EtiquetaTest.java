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
import models.Etiqueta;
import models.EtiquetaRepository;
import models.JPAEtiquetaRepository;

public class EtiquetaTest {
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
  
	private TableroRepository newTableroRepository() {
		return injector.instanceOf(TableroRepository.class);
  }
  
  private EtiquetaRepository newEtiquetaRepository() {
    return injector.instanceOf(EtiquetaRepository.class);
  }

  private UsuarioRepository newUsuarioRepository() {
		return injector.instanceOf(UsuarioRepository.class);
	}

  @Test
  public void testCrearEtiquetaTablero() {
    Usuario usuario = new Usuario("Pepito", "yafunciona@gmail.com");
    Tablero tablero = new Tablero(usuario, "Tablero Nuevo");
    Etiqueta etiqueta = new Etiqueta(tablero, "Refactor", "8dec2e");

    assertEquals(etiqueta.getTablero(), tablero);
  }

  @Test
  public void testJPAFindById() {
    EtiquetaRepository etiquetaRepository = newEtiquetaRepository();
    Etiqueta etiqueta = etiquetaRepository.findById(1000L);
    assertEquals("SGT-2", etiqueta.getNombre());
  }

  @Test
  public void testJPAAdd() {
    EtiquetaRepository etiquetaRepository = newEtiquetaRepository();
    TableroRepository tableroRepository = newTableroRepository();
    UsuarioRepository usuarioRepository = newUsuarioRepository();

    Usuario usuario = new Usuario("Pepito", "yafunciona@gmail.com");
    usuario = usuarioRepository.add(usuario);
    Tablero tablero = new Tablero(usuario, "Tablero Nuevo");
    tablero = tableroRepository.add(tablero);
    Etiqueta nueva = new Etiqueta(tablero, "Refactor", "8dec2e");
    nueva = etiquetaRepository.add(nueva);

    assertEquals(nueva.getTablero(), tablero);
  }

  @Test
  public void testJPAUpdate() {
    EtiquetaRepository etiquetaRepository = newEtiquetaRepository();
    TableroRepository tableroRepository = newTableroRepository();
    
    Tablero tablero = tableroRepository.findById(1000L);
    Etiqueta nueva = new Etiqueta(tablero, "Refactor", "8dec2e");
    nueva = etiquetaRepository.add(nueva);
    nueva.setColor("ffffff");
    nueva = etiquetaRepository.update(nueva);
    assertEquals(nueva.getColor(), "ffffff");
  }

  @Test
  public void testJPADelete() {
    EtiquetaRepository etiquetaRepository = newEtiquetaRepository();
    TableroRepository tableroRepository = newTableroRepository();

    Tablero tablero = tableroRepository.findById(1000L);
    Etiqueta nueva = new Etiqueta(tablero, "Refactor", "8dec2e");
    nueva = etiquetaRepository.add(nueva);
    etiquetaRepository.delete(nueva.getId());

    Etiqueta etiqueta = etiquetaRepository.findById(nueva.getId());

    assertNull(etiqueta);
  }
}