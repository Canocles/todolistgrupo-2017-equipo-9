import org.junit.*;
import static org.junit.Assert.*;

import play.db.jpa.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import java.util.List;
import java.util.ArrayList;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import models.Usuario;
import models.Tarea;
import models.Etiqueta;

import services.UsuarioService;
import services.UsuarioServiceException;
import services.TareaService;
import services.TareaServiceException;

public class TareaServiceTest {
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

   private TareaService newTareaService() {
      return injector.instanceOf(TareaService.class);
   }

   // Test #19: allTareasUsuarioEstanOrdenadas
   @Test
   public void allTareasUsuarioEstanOrdenadas() {
      TareaService tareaService = newTareaService();
      List<Tarea> tareas = tareaService.allTareasUsuario(1000L);
      assertEquals("Renovar DNI", tareas.get(0).getTitulo());
      assertEquals("Práctica 1 MADS", tareas.get(1).getTitulo());
   }

   // Test #20: exceptionSiUsuarioNoExisteRecuperandoSusTareas
   @Test(expected = TareaServiceException.class)
   public void crearNuevoUsuarioLoginRepetidoLanzaExcepcion(){
      TareaService tareaService = newTareaService();
      List<Tarea> tareas = tareaService.allTareasUsuario(2003L);
   }

   // Test #21: nuevaTareaUsuario
   @Test
   public void nuevaTareaUsuario() {
      TareaService tareaService = newTareaService();
      long idUsuario = 1000L;
      tareaService.nuevaTarea(idUsuario, "Pagar el alquiler", null);
      assertEquals(3, tareaService.allTareasUsuario(1000L).size());
   }

  @Test
  public void nuevaTareaTablero() {
    TareaService tareaService = newTareaService();
    long idUsuario = 1000L;
    long idTablero = 1000L;
    tareaService.nuevaTareaTablero(idUsuario, "Pagar el alquiler", null, idTablero);
    assertEquals(1, tareaService.allTareasTablero(1000L).size());
  }

  @Test(expected = TareaServiceException.class)
  public void nuevaTareaTableroNoExistenteTest () {
    TareaService tareaService = newTareaService();
    long idUsuario = 1000L;
    long idTablero = 20L;
    tareaService.nuevaTareaTablero(idUsuario, "Pagar el alquiler", null, idTablero);
  }

  @Test(expected = TareaServiceException.class)
  public void nuevaTareaTableroUsuarioNoexistenteTest () {
    TareaService tareaService = newTareaService();
    long idUsuario = 65L;
    long idTablero = 1000L;
    tareaService.nuevaTareaTablero(idUsuario, "Pagar el alquiler", null, idTablero);
  }

   // Test #22: modificación de tareas
   @Test
   public void modificacionTarea() {
      TareaService tareaService = newTareaService();
      long idTarea = 1000L;
      tareaService.modificaTarea(idTarea, "Pagar el alquiler", null);
      Tarea tarea = tareaService.obtenerTarea(idTarea);
      assertEquals("Pagar el alquiler", tarea.getTitulo());
   }

   // Test #23: borrado tarea
   @Test
   public void borradoTarea() {
      TareaService tareaService = newTareaService();
      long idTarea = 1000L;
      tareaService.borraTarea(idTarea);
      assertNull(tareaService.obtenerTarea(idTarea));
   }

   @Test
   public void asignarEtiqueta() {
	   Long idTarea = 1001L;
	   Long idEtiqueta = 1000L;
	   TareaService tareaService = newTareaService();
	   Tarea tarea = tareaService.obtenerTarea(idTarea);
	   assertEquals(0, tarea.getEtiquetas().size());
	   tareaService.asignarEtiqueta(idTarea, idEtiqueta);
	   tarea = tareaService.obtenerTarea(idTarea);
	   assertEquals(1, tarea.getEtiquetas().size());
   }

   @Test(expected=TareaServiceException.class)
   public void asignarEtiquetaInexistente() {
	   Long idTarea = 1000L;
	   Long idEtiqueta = 5L;
	   TareaService tareaService = newTareaService();
	   tareaService.asignarEtiqueta(idTarea, idEtiqueta);
   }

   @Test
   public void quitarEtiquetaTarea() {
	Long idTarea = 1001L;
	Long idEtiqueta = 1000L;
	TareaService tareaService = newTareaService();
	tareaService.asignarEtiqueta(idTarea, idEtiqueta);
	Tarea  tarea = tareaService.obtenerTarea(idTarea);
	assertEquals(1, tarea.getEtiquetas().size());

	tareaService.quitarEtiquetaTarea(idTarea, idEtiqueta);
	tarea = tareaService.obtenerTarea(idTarea);
	assertEquals(0, tarea.getEtiquetas().size());
   }

	@Test(expected=TareaServiceException.class)
	public void quitarEtiquetaInexistente() {
		Long idTarea = 1000L;
		Long idEtiqueta = 5L;
		TareaService tareaService = newTareaService();
		tareaService.quitarEtiquetaTarea(idTarea, idEtiqueta);
	}
}
