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

import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;

import models.Usuario;
import models.Tarea;

import services.UsuarioService;
import services.UsuarioServiceException;
import services.TareaService;
import services.TareaServiceException;

public class Practica2Test {
  static private Injector injector;

  // Se ejecuta sólo una vez, al principio de todos los tests
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

  private UsuarioService newUsuarioService() {
    return injector.instanceOf(UsuarioService.class);
  }

  private TareaService newTareaService() {
    return injector.instanceOf(TareaService.class);
  }

  @Test
  public void findUsuarioPorIdNullTest() {
    UsuarioService usuarioService = newUsuarioService();
    Usuario usuario = usuarioService.findUsuarioPorId(52L);
    assertNull(usuario);
  }

  @Test(expected = UsuarioServiceException.class)
  public void modificarUsuarioNoExistenteTest() {
    UsuarioService usuarioService = newUsuarioService();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Date fecha = null;
    try {
      fecha = sdf.parse("2017-10-01");
    } catch (ParseException ex) {
      System.out.println(ex);
    }
    Usuario usuario = usuarioService.modificaUsuario("pepito", "memodifico@gmail.com", "Modifico", "el Perfil", fecha);
  }

  @Test(expected = TareaServiceException.class)
  public void borrarTareaNoExistente() {
    TareaService tareaService = newTareaService();
    tareaService.borraTarea(3L);
  }

  @Test(expected = TareaServiceException.class)
  public void modificarTareaNoExistente() {
    TareaService tareaService = newTareaService();
    tareaService.modificaTarea(3L, "Cambio imposible", null);
  }
}
