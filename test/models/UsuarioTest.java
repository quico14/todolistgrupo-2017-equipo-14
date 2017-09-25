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
import models.JPAUsuarioRepository;

public class UsuarioTest {

  static Database db;
  static JPAApi jpaApi;

  // Se ejecuta sólo una vez, al principio de todos los tests
  @BeforeClass
  static public void initDatabase() {
     // Inicializamos la BD en memoria y su nombre JNDI
     db = Databases.inMemoryWith("jndiName", "DBTest");
     db.getConnection();
     // Se activa la compatibilidad MySQL en la BD H2
     db.withConnection(connection -> {
        connection.createStatement().execute("SET MODE MySQL;");
     });
     // Activamos en JPA la unidad de persistencia "memoryPersistenceUnit"
     // declarada en META-INF/persistence.xml y obtenemos el objeto
     // JPAApi
     jpaApi = JPA.createFor("memoryPersistenceUnit");
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
     assertNotNull(jpaApi);
     UsuarioRepository repository = new JPAUsuarioRepository(jpaApi);
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
      String selectStatement = "SELECT NOMBRE FROM USUARIO WHERE ID = ? ";
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
     UsuarioRepository repository = new JPAUsuarioRepository(jpaApi);
     Usuario usuario = repository.findById(1000L);
     assertEquals("juangutierrez", usuario.getLogin());
  }

  // Test 4: testFindUsuarioPorLogin
  @Test
  public void testFindUsuarioPorLogin() {
     UsuarioRepository repository = new JPAUsuarioRepository(jpaApi);
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
     /*<Usuario id="1000" login="juangutierrez" nombre="Juan" apellidos="Gutierrez"
         password="123456789" eMail="juan.gutierrez@gmail.com" fechaNacimiento="1993-12-10"/>*/
      UsuarioRepository repository = new JPAUsuarioRepository(jpaApi);
      Usuario usuario = repository.findByLogin("juangutierrez");
      Usuario usuario1 = new Usuario("juangutierrez", "maria@gmail.com");
      usuario1.setPassword(usuario.getPassword());
      usuario1.setNombre(usuario.getNombre());
      usuario1.setApellidos(usuario.getApellidos());
      usuario1.setFechaNacimiento(usuario.getFechaNacimiento());
      usuario1.setId((Long)usuario.getId());

      repository.edit(usuario1);

      Usuario usuario3 = repository.findByLogin("juangutierrez");
      assertEquals(usuario1, usuario3);
      assertEquals(usuario1.getEmail(), usuario3.getEmail());
      assertEquals(usuario1.getId(), usuario3.getId());
      assertEquals(usuario1.getPassword(), usuario3.getPassword());
      assertEquals(usuario1.getFechaNacimiento(), usuario3.getFechaNacimiento());
      assertEquals(usuario1.getNombre(), usuario3.getNombre());
    }

}
