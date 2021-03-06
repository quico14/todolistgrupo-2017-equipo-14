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

import java.util.List;

import models.Usuario;
import models.Tarea;

import models.UsuarioRepository;
import models.JPAUsuarioRepository;
import models.TareaRepository;
import models.JPATareaRepository;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class TareaTest {

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

  // Test #11: testCrearTarea
  @Test
  public void testCrearTarea() {
     Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
     Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

     assertEquals("juangutierrez", tarea.getUsuario().getLogin());
     assertEquals("juangutierrez@gmail.com", tarea.getUsuario().getEmail());
     assertEquals("Práctica 1 de MADS", tarea.getTitulo());
  }

  // Test #14: testEqualsTareasConId
  @Test
  public void testEqualsTareasConId() {
     Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
     Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS");
     Tarea tarea2 = new Tarea(usuario, "Renovar DNI");
     Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler");
     tarea1.setId(1000L);
     tarea2.setId(1000L);
     tarea3.setId(2L);
     assertEquals(tarea1, tarea2);
     assertNotEquals(tarea1, tarea3);
  }

  // Test #15
  @Test
  public void testEqualsTareasSinId() {
     Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
     Tarea tarea1 = new Tarea(usuario, "Renovar DNI");
     Tarea tarea2 = new Tarea(usuario, "Renovar DNI");
     Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler");
     assertEquals(tarea1, tarea2);
     assertNotEquals(tarea1, tarea3);
  }

  // Test #16: testAddTareaJPARepositoryInsertsTareaDatabase
  @Test
  public void testAddTareaJPARepositoryInsertsTareaDatabase() {
    UsuarioRepository usuarioRepository = newUsuarioRepository();
    TareaRepository tareaRepository = newTareaRepository();
    Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    usuario = usuarioRepository.add(usuario);
    Tarea tarea = new Tarea(usuario, "Renovar DNI");
    tarea = tareaRepository.add(tarea);
    Logger.info("Número de tarea: " + Long.toString(tarea.getId()));
    assertNotNull(tarea.getId());
    assertEquals("Renovar DNI", getTituloFromTareaDB(tarea.getId()));
  }

  private String getTituloFromTareaDB(Long tareaId) {
     String titulo = db.withConnection(connection -> {
        String selectStatement = "SELECT TITULO FROM Tarea WHERE ID = ? ";
        PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
        prepStmt.setLong(1, tareaId);
        ResultSet rs = prepStmt.executeQuery();
        rs.next();
        return rs.getString("TITULO");
     });
     return titulo;
  }

  // Test #17 testFindTareaById
  @Test
  public void testFindTareaPorId() {
     TareaRepository repository = newTareaRepository();
     Tarea tarea = repository.findById(1000L);
     assertEquals("Renovar DNI", tarea.getTitulo());
  }

  // Test #18 testFindAllTareasUsuario
  @Test
  public void testFindAllTareasUsuario() {
    UsuarioRepository repository = newUsuarioRepository();
    Usuario usuario = repository.findById(1000L);
    assertEquals(3, usuario.getTareas().size());
  }

  // Test #45 testTerminada
  @Test
  public void testTerminada() {
    TareaRepository repository = newTareaRepository();
    Tarea tarea = repository.findById(1000L);
    assertFalse(tarea.getTerminada());
  }

  // Test #48 testFechaCreacion
  @Test
  public void testFechaCreacion() throws ParseException {
    TareaRepository repository = newTareaRepository();
    Tarea tarea = repository.findById(1000L);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    assertTrue(tarea.getFechaCreacion().compareTo(sdf.parse("2017-09-09 16:32:00")) == 0);
  }

  // Test #50 testFechaLimite
  @Test
  public void testFechaLimite() throws ParseException {
    TareaRepository repository = newTareaRepository();
    Tarea tarea = repository.findById(1000L);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    assertTrue(tarea.getFechaLimite().compareTo(sdf.parse("2017-12-05")) == 0);
  }

  // Test 59: testTareaTablero
  @Test
  public void testTareaTablero() {
    TareaRepository repository = newTareaRepository();
    Tarea tarea = repository.findById(1000L);

    assertEquals("Tablero test 1", tarea.getTablero().getNombre());
  }

}
