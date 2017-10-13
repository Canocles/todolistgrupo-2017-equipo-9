import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import play.db.jpa.*;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import models.Usuario;

import services.UsuarioService;
import services.UsuarioServiceException;


public class UsuarioServiceTest {
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

   //Test 5: crearNuevoUsuarioCorrectoTest
   @Test
   public void crearNuevoUsuarioCorrectoTest(){
      UsuarioService usuarioService = newUsuarioService();
      Usuario usuario = usuarioService.creaUsuario("luciaruiz", "lucia.ruiz@gmail.com", "123456");
      assertNotNull(usuario.getId());
      assertEquals("luciaruiz", usuario.getLogin());
      assertEquals("lucia.ruiz@gmail.com", usuario.getEmail());
      assertEquals("123456", usuario.getPassword());
   }

   //Test 6: crearNuevoUsuarioLoginRepetidoLanzaExcepcion
   @Test(expected = UsuarioServiceException.class)
   public void crearNuevoUsuarioLoginRepetidoLanzaExcepcion(){
      UsuarioService usuarioService = newUsuarioService();
      // En la BD de prueba usuarios_dataset se ha cargado el usuario juangutierrez
      Usuario usuario = usuarioService.creaUsuario("juangutierrez", "juan.gutierrez@gmail.com", "123456");
   }

   //Test 7: findUsuarioPorLogin
   @Test
   public void findUsuarioPorLogin() {
      UsuarioService usuarioService = newUsuarioService();
      // En la BD de prueba usuarios_dataset se ha cargado el usuario juangutierrez
      Usuario usuario = usuarioService.findUsuarioPorLogin("juangutierrez");
      assertNotNull(usuario);
      assertEquals((Long) 1000L, usuario.getId());
   }


   //Test 8: loginUsuarioExistenteTest
   @Test
   public void loginUsuarioExistenteTest() {
      UsuarioService usuarioService = newUsuarioService();
      // En la BD de prueba usuarios_dataset se ha cargado el usuario juangutierrez
      Usuario usuario = usuarioService.login("juangutierrez", "123456789");
      assertEquals((Long) 1000L, usuario.getId());
   }

   //Test 9: loginUsuarioNoExistenteTest
   @Test
   public void loginUsuarioNoExistenteTest() {
      UsuarioService usuarioService = newUsuarioService();
      // En la BD de prueba usuarios_dataset se ha cargado el usuario juangutierrez
      Usuario usuario = usuarioService.login("juan", "123456789");
      assertNull(usuario);
   }

   //Test 10: findUsuarioPorId
   @Test
   public void findUsuarioPorId() {
      UsuarioService usuarioService = newUsuarioService();
      // En la BD de prueba usuarios_dataset se ha cargado el usuario juangutierrez
      Usuario usuario = usuarioService.findUsuarioPorId(1000L);
      assertNotNull(usuario);
      assertEquals("juangutierrez", usuario.getLogin());
   }

   // Test 11: modificación de perfil
   @Test
   public void modificacionUsuario() {
     UsuarioService usuarioService = newUsuarioService();
     String login = "juangutierrez";

     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     Date fecha = null;
     try {
       fecha = sdf.parse("2017-10-01");
     } catch (ParseException ex) {
       System.out.println(ex);
     }
     usuarioService.modificaUsuario(login, "memodifico@gmail.com", "Modifico", "el Perfil", fecha);
     Usuario usuario = usuarioService.findUsuarioPorLogin(login);
     assertEquals("memodifico@gmail.com", usuario.getEmail());
   }
}
