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

import services.GrupoService;
import services.GrupoServiceException;
import services.UsuarioService;
import services.UsuarioServiceException;

import java.util.Set;
import java.util.List;

import play.db.jpa.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import models.GrupoRepository;

public class GrupoServiceTest {
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

  private GrupoService newGrupoService() {
     return injector.instanceOf(GrupoService.class);
  }

  private UsuarioService newUsuarioService() {
     return injector.instanceOf(UsuarioService.class);
  }

  private GrupoRepository newGrupoRepository() {
     return injector.instanceOf(GrupoRepository.class);
  }

  //Test #63 testCrearGrupoService
  @Test
  public void nuevoGrupo() {
     GrupoService grupoService = newGrupoService();
     GrupoRepository grupoRepository = newGrupoRepository();
     long idUsuario = 1000L;
     Grupo grupoCreado = grupoService.nuevoGrupo(idUsuario, "Grupo hogar");
     Grupo grupoBD = grupoRepository.findById(grupoCreado.getId());

     assertEquals(grupoCreado, grupoBD);
  }

  // Test #64: allTareasUsuarioEstanOrdenadas
  @Test
  public void allGruposUsuarioEstanOrdenadas() {
     GrupoService grupoService = newGrupoService();
     List<Grupo> grupos = grupoService.allGruposUsuario(1000L);
     assertEquals("Grupo test 1", grupos.get(0).getNombre());
     assertEquals("Grupo test 2", grupos.get(1).getNombre());
  }

  // Test #65: usuarioNoExisteRecuperandoAdministrados
  @Test(expected = GrupoServiceException.class)
  public void usuarioNoExisteRecuperandoAdministrados(){
     GrupoService grupoService = newGrupoService();
     List<Grupo> grupos = grupoService.allGruposUsuario(1023301L);
  }

  // Test #66: usuarioParticipaGrupo
  @Test
  public void usuarioParticipaGrupo(){
    GrupoService grupoService = newGrupoService();
    Grupo grupo = grupoService.allGruposUsuario(1000L).get(0);

    Grupo grupoActualizado = grupoService.addParticipante("juangutierrez2", grupo);
    Grupo grupoQuery = grupoService.allGruposUsuario(1000L).get(0);

    assertEquals(grupoActualizado, grupoQuery);
  }

  // Test #67: allGruposParticipando
  @Test
  public void allGruposParticipando() {
     GrupoService grupoService = newGrupoService();

     Grupo grupo = grupoService.allGruposUsuario(1000L).get(0);
     Grupo grupo2 = grupoService.allGruposUsuario(1000L).get(1);

     assertEquals(0, grupoService.getGrupos(1001L).size());
     grupoService.addParticipante("juangutierrez2", grupo);
     assertEquals(grupo, grupoService.getGrupos(1001L).get(0));
     assertEquals(1, grupoService.getGrupos(1001L).size());
     grupoService.addParticipante("juangutierrez2", grupo2);
     assertEquals(2, grupoService.getGrupos(1001L).size());
  }

  // Test #68: usuarioBorradoGrupo
  @Test
  public void usuarioBorradoGrupo(){
    GrupoService grupoService = newGrupoService();
    Grupo grupo = grupoService.allGruposUsuario(1000L).get(0);

    Grupo grupoActualizado = grupoService.addParticipante("juangutierrez2", grupo);
    Grupo grupoQuery = grupoService.allGruposUsuario(1000L).get(0);

    assertEquals(grupoActualizado, grupoQuery);

    grupoActualizado = grupoService.removeParticipante(1001L, grupo);
    grupoQuery = grupoService.allGruposUsuario(1000L).get(0);

    assertEquals(grupoActualizado, grupoQuery);
  }

  // Test #69: findGrupoPorId
  @Test
  public void findGrupoPorId(){
    GrupoService grupoService = newGrupoService();
    Grupo grupo = grupoService.findGrupoPorId(1000L);

    assertEquals("Grupo test 1", grupo.getNombre());
  }

}
