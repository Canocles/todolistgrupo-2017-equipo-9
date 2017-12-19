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

import models.Etiqueta;
import services.EtiquetaService;

import models.Tablero;
import services.TableroService;
import services.TableroServiceException;

public class EtiquetaServiceTest {
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

	private EtiquetaService newEtiquetaService() {
		return injector.instanceOf(EtiquetaService.class);
  }

  @Test
	public void getEtiquetasTableroTest() {
		EtiquetaService etiquetaService = newEtiquetaService();
		List<Etiqueta> etiquetas = etiquetaService.getEtiquetasTablero(1000L);
		assertEquals(1, etiquetas.size());
	}
}