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
import models.Grupo;
import models.GrupoRepository;
import models.UsuarioRepository;

import java.util.Set;
import java.util.List;

import play.db.jpa.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

public class GrupoTest {
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

  private void initDataSet() throws Exception {
    JndiDatabaseTester databaseTester = new JndiDatabaseTester("DBTest");
    IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("test/resources/usuarios_dataset.xml"));
    databaseTester.setDataSet(initialDataSet);
    databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
    databaseTester.onSetup();
  }

  //Test #32
  @Test
  public void testCrearGrupo() {
    Usuario usuario = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    Grupo grupo = new Grupo(usuario, "Grupo 1");

    assertEquals("juangutierrez", grupo.getAdministrador().getLogin());
    assertEquals("juangutierrez@gmail.com", grupo.getAdministrador().getEmail());
    assertEquals("Grupo 1", grupo.getNombre());
  }

  //Test #33
  @Test
  public void testObtenerGrupoRepository() {
    GrupoRepository grupoRepository = injector.instanceOf(GrupoRepository.class);
    assertNotNull(grupoRepository);
  }

  //Test #34
  @Test
  public void testCrearTablaGrupoEnBD() throws Exception {
    Connection connection = db.getConnection();
    DatabaseMetaData meta = connection.getMetaData();
    // En la BD H2 el nombre de las tablas se define con mayúscula y en
    // MySQL con minúscula
    ResultSet resH2 = meta.getTables(null, null, "GRUPO", new String[] {"TABLE"});
    ResultSet resMySQL = meta.getTables(null, null, "Grupo", new String[] {"TABLE"});
    boolean existeTabla = resH2.next() || resMySQL.next();
    assertTrue(existeTabla);
  }

  //Test #35
  @Test
  public void testAddGrupoInsertsDatabase() {
    UsuarioRepository usuarioRepository = injector.instanceOf(UsuarioRepository.class);
    GrupoRepository grupoRepository = injector.instanceOf(GrupoRepository.class);
    Usuario administrador = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    administrador = usuarioRepository.add(administrador);
    Grupo grupo = new Grupo(administrador, "Grupo 1");
    grupo = grupoRepository.add(grupo);
    assertNotNull(grupo.getId());
    assertEquals("Grupo 1", getNombreFromGrupoDB(grupo.getId()));
  }

  private String getNombreFromGrupoDB(Long grupoId) {
    String nombre = db.withConnection(connection -> {
       String selectStatement = "SELECT Nombre FROM Grupo WHERE ID = ? ";
       PreparedStatement prepStmt = connection.prepareStatement(selectStatement);
       prepStmt.setLong(1, grupoId);
       ResultSet rs = prepStmt.executeQuery();
       rs.next();
       return rs.getString("Nombre");
    });
    return nombre;
  }

  // Test #36 testUsuarioAdministraVariosTableros
  @Test
  public void testUsuarioAdministraVariosGrupos() {
    UsuarioRepository usuarioRepository = injector.instanceOf(UsuarioRepository.class);
    GrupoRepository grupoRepository = injector.instanceOf(GrupoRepository.class);
    Usuario administrador = new Usuario("juangutierrez", "juangutierrez@gmail.com");
    administrador = usuarioRepository.add(administrador);
    Grupo grupo1 = new Grupo(administrador, "Grupo 1");
    grupoRepository.add(grupo1);
    Grupo grupo2 = new Grupo(administrador, "Grupo 2");
    grupoRepository.add(grupo2);
    // Recuperamos el administrador del repository
    administrador = usuarioRepository.findById(administrador.getId());
    // Y comprobamos si tiene los tableros
    assertEquals(2, administrador.getGruposAdministrados().size());
  }

  // Test #37 testUsuarioParticipaEnVariosTableros
  @Test
  public void testUsuarioParticipaEnVariosGrupos() throws Exception {
    initDataSet();
    UsuarioRepository usuarioRepository = injector.instanceOf(UsuarioRepository.class);
    GrupoRepository grupoRepository = injector.instanceOf(GrupoRepository.class);
    Usuario admin = usuarioRepository.findById(1000L);
    Usuario usuario = usuarioRepository.findById(1001L);
    Set<Grupo> grupos = admin.getGruposAdministrados();
    // Tras cargar los datos del dataset el usuario2 no tiene ningún
    // tablero asociado y el usuario 1 tiene 2 tableros administrados
    assertEquals(0, usuario.getTableros().size());
    assertEquals(2, grupos.size());
    for (Grupo grupo : grupos) {
       // Actualizamos la relación en memoria, añadiendo el usuario
       // al tablero
       grupo.getParticipantes().add(usuario);
       // Actualizamos la base de datos llamando al repository
       grupoRepository.update(grupo);
    }
    // Comprobamos que se ha actualizado la relación en la BD y
    // el usuario pertenece a los dos tableros a los que le hemos añadido
    usuario = usuarioRepository.findById(1001L);
    Set<Grupo> gruposUsuario = usuario.getGrupos();
    assertEquals(2, gruposUsuario.size());
    for (Grupo grupo : grupos) {
       assertTrue(gruposUsuario.contains(grupo));
    }
  }

  // Test #38 testTableroTieneVariosUsuarios
  @Test
  public void testGrupoTieneVariosUsuarios() throws Exception {
    initDataSet();
    UsuarioRepository usuarioRepository = injector.instanceOf(UsuarioRepository.class);
    GrupoRepository grupoRepository = injector.instanceOf(GrupoRepository.class);
    // Obtenemos datos del dataset
    Grupo grupo = grupoRepository.findById(1000L);
    Usuario usuario1 = usuarioRepository.findById(1000L);
    Usuario usuario2 = usuarioRepository.findById(1001L);
    Usuario usuario3 = usuarioRepository.findById(1002L);
    assertEquals(0, grupo.getParticipantes().size());
    assertEquals(0, usuario1.getTableros().size());
    // Añadimos los 3 usuarios al tablero
    grupo.getParticipantes().add(usuario1);
    grupo.getParticipantes().add(usuario2);
    grupo.getParticipantes().add(usuario3);
    grupoRepository.update(grupo);
    // Comprobamos que los datos se han actualizado
    grupo = grupoRepository.findById(1000L);
    usuario1 = usuarioRepository.findById(1000L);
    assertEquals(3, grupo.getParticipantes().size());
    assertEquals(1, usuario1.getGrupos().size());
    assertTrue(grupo.getParticipantes().contains(usuario1));
    assertTrue(usuario1.getGrupos().contains(grupo));
  }
}
