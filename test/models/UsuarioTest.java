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


import models.Usuario;
import models.UsuarioRepository;
import models.Tablero;
import models.TableroRepository;
import models.JPAUsuarioRepository;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

public class UsuarioTest {

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

    private UsuarioRepository newUsuarioRepository() {
       return injector.instanceOf(UsuarioRepository.class);
     }

    // Test 1: testCrearUsuario

  @Test
  public void testCrearUsuario() throws ParseException {
    // Los parámetros del constructor son los campos obligatorios
    Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    usuario.setNombre("Juan");
    usuario.setApellidos("Gutierrez");
    usuario.setPassword("123456789");

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    usuario.setFechaNacimiento(sdf.parse("1997-02-20"));

    assertEquals("juangutierrez", usuario.getLogin());
    assertEquals("juangutierrez@gmail.com", usuario.getEmail());
    assertEquals("Juan", usuario.getNombre());
    assertEquals("Gutierrez", usuario.getApellidos());
    assertEquals("123456789", usuario.getPassword());
    assertTrue(usuario.getFechaNacimiento().compareTo(sdf.parse("1997-02-20")) == 0);
  }

   // Test 2: testAddUsuarioJPARepositoryInsertsUsuarioDatabase
  @Test
  public void testAddUsuarioJPARepositoryInsertsUsuarioDatabase() {
     UsuarioRepository repository = newUsuarioRepository();
     Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
     usuario.setNombre("Juan");
     usuario.setApellidos("Gutierrez");
     usuario.setPassword("123456789");
     usuario = repository.add(usuario);
     Logger.info("Número de usuario: " + Long.toString(usuario.getId()));
     assertNotNull(usuario.getId());
     assertEquals("Juan", getNombreFromUsuarioDB(usuario.getId()));
  }

  private String getNombreFromUsuarioDB(Long usuarioId) {
   String nombre = db.withConnection(connection -> {
      String selectStatement = "SELECT NOMBRE FROM Usuario WHERE ID = ? ";
      PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
      prepStmt.setLong(1, usuarioId);
      ResultSet rs = prepStmt.executeQuery();
      rs.next();
      return rs.getString("NOMBRE");
   });
   return nombre;
  }


  // Test 3: testFindUsuarioPorId
  @Test
  public void testFindUsuarioPorId() {
     UsuarioRepository repository = newUsuarioRepository();
     Usuario usuario = repository.findById(1000L);
     assertEquals("juangutierrez", usuario.getLogin());
  }

  // Test 4: testFindUsuarioPorLogin
  @Test
  public void testFindUsuarioPorLogin() {
     UsuarioRepository repository = newUsuarioRepository();
     Usuario usuario = repository.findByLogin("juangutierrez");
     assertEquals((Long) 1000L, usuario.getId());
  }

  // Test 12: testEqualsUsuariosConId
  @Test
  public void testEqualsUsuariosConId() {
     Usuario usuario1 = new Usuario("juangutierrez", "juangutierrez@gmail.com");
     Usuario usuario2 = new Usuario("mariafernandez", "mariafernandez@gmail.com");
     Usuario usuario3 = new Usuario("antoniolopez", "antoniolopez@gmail.com");
     usuario1.setId(1L);
     usuario2.setId(1L);
     usuario3.setId(2L);
     assertEquals(usuario1, usuario2);
     assertNotEquals(usuario1, usuario3);
  }

  // Test 13: testEqualsUsuariosSinId
  @Test
  public void testEqualsUsuariosSinId() {
    Usuario usuario1 = new Usuario("mariafernandez", "mariafernandez@gmail.com");
    Usuario usuario2 = new Usuario("mariafernandez", "mariafernandez@gmail.com");
    Usuario usuario3 = new Usuario("antoniolopez", "antoniolopez@gmail.com");
    assertEquals(usuario1, usuario2);
    assertNotEquals(usuario1, usuario3);
  }

  // Test 24: testUpdatedUsuario
  @Test
  public void testUpdatedUsuario() {
    UsuarioRepository repository = newUsuarioRepository();
    Usuario usuario = repository.findByLogin("juangutierrez");
    Usuario usuario1 = new Usuario("juangutierrez", "maria@gmail.com");
    usuario1.setPassword(usuario.getPassword());
    usuario1.setNombre(usuario.getNombre());
    usuario1.setApellidos(usuario.getApellidos());
    usuario1.setFechaNacimiento(usuario.getFechaNacimiento());
    usuario1.setId((Long)usuario.getId());

    repository.edit(usuario1);

    Usuario usuario3 = repository.findByLogin("juangutierrez");

    if (usuario1.equals(usuario3)) {
      System.out.println("usuario1 is equal to usuario3");
    } else {
      System.out.println("usuario1 is not equal to usuario3");
    }

    assertEquals(usuario1, usuario3);
  }

  // Test #36 testUsuarioAdministraVariosTableros
  @Test
  public void testUsuarioAdministraVariosTableros() {
    UsuarioRepository usuarioRepository = injector.instanceOf(UsuarioRepository.class);
    TableroRepository tableroRepository = injector.instanceOf(TableroRepository.class);
    Usuario administrador = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    administrador = usuarioRepository.add(administrador);
    Tablero tablero1 = new Tablero(administrador, "Tablero 1");
    tableroRepository.add(tablero1);
    Tablero tablero2 = new Tablero(administrador, "Tablero 2");
    tableroRepository.add(tablero2);
    // Recuperamos el administrador del repository
    administrador = usuarioRepository.findById(administrador.getId());
    // Y comprobamos si tiene los tableros
    assertEquals(2, administrador.getAdministrados().size());
  }

}
