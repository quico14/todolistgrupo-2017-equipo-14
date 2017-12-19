import org.junit.*;
import static org.junit.Assert.*;

import play.db.jpa.*;

import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import org.dbunit.operation.*;
import java.io.FileInputStream;

import java.util.List;

import models.Usuario;
import models.Tarea;
import models.Comentario;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import services.UsuarioService;
import services.UsuarioServiceException;
import services.TareaService;
import services.TareaServiceException;
import services.ComentarioService;
import services.ComentarioServiceException;

public class ComentarioServiceTest {
  static private Injector injector;

  @BeforeClass
  static public void initApplication() {
     GuiceApplicationBuilder guiceApplicationBuilder =
         new GuiceApplicationBuilder().in(Environment.simple());
     injector = guiceApplicationBuilder.injector();
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

  private ComentarioService newComentarioService() {
     return injector.instanceOf(ComentarioService.class);
  }

  // Test #89: nuevoComentarioService
  @Test
  public void nuevoComentarioService() {
     ComentarioService comentarioService = newComentarioService();
     long idUsuario = 1000L;
     long idTarea = 1000L;
     comentarioService.nuevoComentario(idUsuario, idTarea, "Descripcion del comentario");
     assertEquals(3, comentarioService.allComentarios(idTarea).size());
  }

  // Test #90: modificacionComentario
  @Test
  public void modificacionComentario() {
     ComentarioService comentarioService = newComentarioService();
     long idComentario = 1000L;
     comentarioService.modificaComentario(idComentario, "Descripcion editada");
     Comentario comentario = comentarioService.findComentarioPorId(idComentario);
     assertEquals("Descripcion editada", comentario.getDescripcion());
  }

  // Test #91: borradoComentario
  @Test(expected = ComentarioServiceException.class)
  public void borradoComentario(){
     ComentarioService comentarioService = newComentarioService();
     long idComentario = 1000L;
     comentarioService.borraComentario(idComentario);
     assertNull(comentarioService.findComentarioPorId(idComentario));
  }

}
