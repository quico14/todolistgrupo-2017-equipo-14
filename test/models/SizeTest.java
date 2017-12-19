import org.junit.*;
import static org.junit.Assert.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;

import play.Logger;

import java.sql.*;

import org.junit.*;
import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;


import models.Size;
import models.SizeRepository;
import models.Tablero;
import models.TableroRepository;
import models.JPAUsuarioRepository;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

public class SizeTest {

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


    // Se ejecuta al antes de cada test
    // Se insertan los datos de prueba en la tabla Usuarios de
    // la BD "DBTest". La BD ya contiene una tabla de usuarios
    // porque la ha creado JPA al tener la propiedad
    // hibernate.hbm2ddl.auto (en META-INF/persistence.xml) el valor update
    // Los datos de prueba se definen en el fichero
    // test/resources/usuarios_dataset.xml
    @Before
    public void initData() throws Exception {
       JndiDatabaseTester databaseTester = new JndiDatabaseTester("DBTest");
       IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new
       FileInputStream("test/resources/usuarios_dataset.xml"));
       databaseTester.setDataSet(initialDataSet);

       // Definimos como operación SetUp CLEAN_INSERT, que hace un
       // DELETE_ALL de todas las tablase del dataset, seguido por un
       // INSERT. (http://dbunit.sourceforge.net/components.html)
       // Es lo que hace DbUnit por defecto, pero así queda más claro.
       databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
       databaseTester.onSetup();
    }

    private SizeRepository newSizeRepository() {
       return injector.instanceOf(SizeRepository.class);
     }

    // Test 72: testCrearSize

  @Test
  public void testCrearSize() throws ParseException {
    // Los parámetros del constructor son los campos obligatorios
    Size size = new Size("Medium-Small");

    assertEquals("Medium-Small", size.getNombre());
  }

   // Test 73: testAddSizeJPARepositoryInsertsSizeDatabase
  @Test
  public void testAddSizeJPARepositoryInsertsSizeDatabase() {
     SizeRepository repository = newSizeRepository();
     Size size = new Size("Medium-Small");
     size = repository.add(size);
     assertNotNull(size.getId());
     assertEquals("Medium-Small", getNombreFromSizeDB(size.getId()));
  }

  private String getNombreFromSizeDB(Long sizeId) {
   String nombre = db.withConnection(connection -> {
      String selectStatement = "SELECT NOMBRE FROM Size WHERE ID = ? ";
      PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
      prepStmt.setLong(1, sizeId);
      ResultSet rs = prepStmt.executeQuery();
      rs.next();
      return rs.getString("NOMBRE");
   });
   return nombre;
  }


  // Test 74: testFindSizePorId
  @Test
  public void testFindSizePorId() {
     SizeRepository repository = newSizeRepository();
     Size size = repository.findById(1000L);
     assertEquals("Small", size.getNombre());
  }

  // Test 75: testFindSizePorNombre
  @Test
  public void testFindSizePorNombre() {
     SizeRepository repository = newSizeRepository();
     Size size = repository.findByName("Small");
     assertEquals((Long) 1000L, size.getId());
  }

  // Test 76: testEqualsSizesConId
  @Test
  public void testEqualsUsuariosConId() {
     Size size1 = new Size("1");
     Size size2 = new Size("2");
     Size size3 = new Size("3");
     size1.setId(1L);
     size2.setId(1L);
     size3.setId(2L);
     assertEquals(size1, size2);
     assertNotEquals(size1, size3);
  }

  // Test 77: testEqualsSizesSinId
  @Test
  public void testEqualsSizesSinId() {
    Size size1 = new Size("1");
    Size size2 = new Size("1");
    Size size3 = new Size("3");
    assertEquals(size1, size2);
    assertNotEquals(size1, size3);
  }

  // Test 78: testUpdatedSize
  @Test
  public void testUpdatedSize() {
    SizeRepository repository = newSizeRepository();
    Size size = repository.findByName("Small");
    Size size1 = new Size("small-medium");
    size1.setId((Long)size.getId());

    repository.edit(size1);

    Size size3 = repository.findByName("small-medium");

    if (size1.equals(size3)) {
      System.out.println("usuario1 is equal to usuario3");
    } else {
      System.out.println("usuario1 is not equal to usuario3");
    }

    assertEquals(size1, size3);
  }
}
