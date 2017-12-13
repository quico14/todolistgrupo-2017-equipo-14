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
import models.Comentario;

import models.UsuarioRepository;
import models.JPAUsuarioRepository;
import models.TareaRepository;
import models.JPATareaRepository;
import models.ComentarioRepository;
import models.JPAComentarioRepository;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

public class ComentarioTest {

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

    private ComentarioRepository newComentarioRepository() {
       return injector.instanceOf(ComentarioRepository.class);
    }

  // Test #83: testCrearComentario
  @Test
  public void testCrearComentario() {
     Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
     Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");
     Comentario comentario = new Comentario(usuario, tarea, "Funciona correctamente");

     assertEquals("juangutierrez", comentario.getCreador().getLogin());
     assertEquals("Práctica 1 de MADS", comentario.getTareaId().getTitulo());
     assertEquals("Funciona correctamente", comentario.getDescripcion());
  }

  // Test #84: testEqualsComentariosConId
  @Test
  public void testEqualsComentariosConId() {
     Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
     Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");
     Comentario comentario1 = new Comentario(usuario, tarea, "Funciona correctamente");
     Comentario comentario2 = new Comentario(usuario, tarea, "Comentario 2");
     Comentario comentario3 = new Comentario(usuario, tarea, "Comentario 3");
     comentario1.setId(1000L);
     comentario2.setId(1000L);
     comentario3.setId(2L);

     assertEquals(comentario1, comentario2);
     assertNotEquals(comentario1, comentario3);
  }

  // Test #85: testEqualsComentariosSinId
  @Test
  public void testEqualsComentariosSinId() {
    Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");
    Comentario comentario1 = new Comentario(usuario, tarea, "Funciona correctamente");
    Comentario comentario2 = new Comentario(usuario, tarea, "Funciona correctamente");
    Comentario comentario3 = new Comentario(usuario, tarea, "Comentario 3");

    assertEquals(comentario1, comentario2);
    assertNotEquals(comentario1, comentario3);
  }

  // Test #86: testAddComentarioJPARepositoryInsertsTareaDatabase
  @Test
  public void testAddComentarioJPARepositoryInsertsTareaDatabase() {
    UsuarioRepository usuarioRepository = newUsuarioRepository();
    TareaRepository tareaRepository = newTareaRepository();
    ComentarioRepository comentarioRepository = newComentarioRepository();

    Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    usuario = usuarioRepository.add(usuario);
    Tarea tarea = new Tarea(usuario, "Renovar DNI");
    tarea = tareaRepository.add(tarea);
    Comentario comentario = new Comentario(usuario, tarea, "Funciona correctamente");
    comentario = comentarioRepository.add(comentario);

    assertNotNull(comentario.getId());
    assertEquals("Funciona correctamente", getDescripcionFromComentarioDB(comentario.getId()));
  }

  private String getDescripcionFromComentarioDB(Long comentarioId) {
     String descripcion = db.withConnection(connection -> {
        String selectStatement = "SELECT DESCRIPCION FROM Comentario WHERE ID = ? ";
        PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
        prepStmt.setLong(1, comentarioId);
        ResultSet rs = prepStmt.executeQuery();
        rs.next();
        return rs.getString("DESCRIPCION");
     });
     return descripcion;
  }

  // Test #87 testFindComentarioById
  @Test
  public void testFindComentarioPorId() {
     ComentarioRepository repository = newComentarioRepository();
     Comentario comentario = repository.findById(1000L);
     assertEquals("Es correcto", comentario.getDescripcion());
  }

  // Test #88 testDeleteComentario
  @Test
  public void testDeleteComentario() {
    ComentarioRepository repository = newComentarioRepository();
    Comentario comentario = repository.findById(1000L);
    assertNotNull(comentario.getId());

    repository.delete(1000L);
    comentario = repository.findById(1000L);
    assertNull(comentario);
  }

}
