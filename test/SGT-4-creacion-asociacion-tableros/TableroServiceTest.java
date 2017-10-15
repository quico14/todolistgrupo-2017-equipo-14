import org.junit.*;
import static org.junit.Assert.*;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import play.db.jpa.*;

import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import play.db.Database;
import play.db.Databases;

import java.sql.*;

import models.Usuario;
import models.Tablero;
import models.TableroRepository;
import models.UsuarioRepository;

import services.TableroService;
import services.TableroServiceException;

import java.util.Set;
import java.util.List;

import play.db.jpa.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import models.TableroRepository;

public class TableroServiceTest {
  static private Injector injector;
  static Database db;

  @BeforeClass
  static public void initApplication() {
    GuiceApplicationBuilder guiceApplicationBuilder =
        new GuiceApplicationBuilder().in(Environment.simple());
    injector = guiceApplicationBuilder.injector();
    // Necesario para inicializar JPA
    injector.instanceOf(JPAApi.class);
    db = injector.instanceOf(Database.class);
  }

  @Before
  public void initDataSet() throws Exception {
    JndiDatabaseTester databaseTester = new JndiDatabaseTester("DBTest");
    IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("test/resources/usuarios_dataset.xml"));
    databaseTester.setDataSet(initialDataSet);
    databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
    databaseTester.onSetup();
  }

  private TableroService newTableroService() {
     return injector.instanceOf(TableroService.class);
  }

  private TableroRepository newTableroRepository() {
     return injector.instanceOf(TableroRepository.class);
  }

  //Test #39 testCrearTableroService
  @Test
  public void nuevoTablero() {
     TableroService tableroService = newTableroService();
     TableroRepository tableroRepository = newTableroRepository();
     long idUsuario = 1000L;
     Tablero tableroCreado = tableroService.nuevoTablero(idUsuario, "Tablero hogar");
     Tablero tableroBD = tableroRepository.findById(tableroCreado.getId());

     assertEquals(tableroCreado, tableroBD);
  }

}
