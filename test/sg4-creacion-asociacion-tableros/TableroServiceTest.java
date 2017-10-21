import org.junit.*;
import static org.junit.Assert.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;

import java.io.FileInputStream;
import java.util.List;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;
import play.db.jpa.*;

import models.Usuario;
import models.Tablero;

import services.UsuarioService;
import services.UsuarioServiceException;
import services.TableroService;
import services.TareaServiceException;

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
    Tablero tablero = tableroService.crearTableroUsuario("Tablero test 3", idUsuario);
    assertEquals(3, tablero.getAdministrador().getAdministrados().size());
  }

  @Test(expected = UsuarioServiceException.class)
  public void crearTableroUsuarioNoExistenteTest () {
    TableroService tableroService = newTableroService();
    long idUsuario = 1004L;
    Tablero tablero = tableroService.crearTableroUsuario("Tablero test 3", idUsuario);
  }

  @Test
  public void obtenerTablerosUsuarioTest () {
    TableroService tableroService = newTableroService();
    List<Tablero> tableros = tableroService.obtenerTablerosAdministradosUsuario(1000L);
    assertEquals("Tablero test 1", tableros.get(0).getNombre());
    assertEquals("Tablero test 2", tableros.get(1).getNombre());
  }

  @Test(expected = UsuarioServiceException.class)
  public void obtenerTablerosUsuarioNoExistenteTest () {
    TableroService tableroService = newTableroService();
    List<Tablero> tableros = tableroService.obtenerTablerosAdministradosUsuario(1004L);
  }

  @Test
  public void allTareasUsuarioEstanOrdenadas () {
    TableroService tableroService = newTableroService();
    List<Tablero> tableros = tableroService.allTablerosUsuario(1000L);
    assertEquals("Tablero test 1", tableros.get(0).getNombre());
    assertEquals("Tablero test 2", tableros.get(1).getNombre());
  }

  @Test
  public void anyadirParticipanteTableroTest () {
    TableroService tableroService = newTableroService();
    Tablero tablero = tableroService.anyadirParticipanteTablero(1000L, 1001L);
    assertEquals(1, tablero.getParticipantes().size());
  }
 }
