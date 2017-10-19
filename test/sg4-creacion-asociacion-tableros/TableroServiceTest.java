import org.junit.*;
import static org.junit.Assert.*;

import play.db.jpa.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import models.Usuario;
import models.UsuarioRepository;
import models.JPAUsuarioRepository;

import models.Tablero;
import models.TableroRepository;
import models.JPATableroRepository;

import services.UsuarioService;
import services.TableroService;

public class TableroServiceTest {
  static private Injector injector;

  @BeforeClass
  static public void initApplication() {
    GuiceApplicationBuilder guiceApplicationBuilder =
         new GuiceApplicationBuilder().in(Environment.simple());
    injector = guiceApplicationBuilder.injector();
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

  private TableroService newTableroService() {
    return injector.instanceOf(TableroService.class);
  }

  @Test
  public void crearTableroUsuarioTest () {
    TableroService tableroService = newTableroService();
    long idUsuario = 1000L;
    Tablero tablero = tableroService.crearTableroUsuario("Primer tablero", idUsuario);
    assertEquals(3, tablero.getAdministrador().getAdministrados().size());
  }
 }
