import org.junit.*;
import static org.junit.Assert.*;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;

import play.Logger;

import java.sql.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import play.db.jpa.JPAApi;

import java.util.List;

import models.Usuario;
import models.Tarea;

import models.UsuarioRepository;
import models.TareaRepository;

import services.UsuarioService;
import services.UsuarioServiceException;
import services.TareaService;
import services.TareaServiceException;

public class Practica2Test {

  static Database db;
  static private Injector injector;

  // Se ejecuta sólo una vez, al principio de todos los tests
  @BeforeClass
  static public void initApplication() {
       GuiceApplicationBuilder guiceApplicationBuilder =
           new GuiceApplicationBuilder().in(Environment.simple());
       injector = guiceApplicationBuilder.injector();
       db = injector.instanceOf(Database.class);
       // Necesario para inicializar JPA
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

  private TareaService newTareaService() {
     return injector.instanceOf(TareaService.class);
  }

  private UsuarioService newUsuarioService() {
     return injector.instanceOf(UsuarioService.class);
  }

  // Test #28: testFindUsuarioPorIdInexistente
  @Test
  public void testFindUsuarioPorIdInexistente() {
    UsuarioService usuarioService = newUsuarioService();
    assertNull(usuarioService.findUsuarioPorId(10234L));
  }

  // Test #29: testBorraTareaInexistente
  @Test(expected = TareaServiceException.class)
  public void testBorraTareaInexistente() {
    TareaService tareaService = newTareaService();

    tareaService.borraTarea(27624786L);
  }

  // Test #30: modificacionTareaInexistente
  @Test(expected = TareaServiceException.class)
  public void modificacionTarea() {
     TareaService tareaService = newTareaService();
     long idTarea = 9837598;
     tareaService.modificaTarea(idTarea, "Pagar el alquiler");
  }

  //Test 31: findUsuarioPorLoginInexistente
  @Test
  public void findUsuarioPorLoginInexistente() {
     UsuarioService usuarioService = newUsuarioService();

     assertNull(usuarioService.findUsuarioPorLogin("UsuarioNoExistente"));
  }
}
