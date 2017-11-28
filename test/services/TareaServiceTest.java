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

import models.Tablero;

import play.inject.guice.GuiceApplicationBuilder;
import play.inject.Injector;
import play.inject.guice.GuiceInjectorBuilder;
import play.Environment;

import services.UsuarioService;
import services.UsuarioServiceException;
import services.TareaService;
import services.TareaServiceException;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class TareaServiceTest {
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

  private TareaService newTareaService() {
     return injector.instanceOf(TareaService.class);
  }

  // Test #19: allTareasUsuarioEstanOrdenadas
  @Test
  public void allTareasUsuarioEstanOrdenadas() {
     TareaService tareaService = newTareaService();
     List<Tarea> tareas = tareaService.allTareasUsuario(1000L);
     assertEquals("Renovar DNI", tareas.get(0).getTitulo());
     assertEquals("Práctica 1 MADS", tareas.get(1).getTitulo());
  }

  // Test #20: exceptionSiUsuarioNoExisteRecuperandoSusTareas
  @Test(expected = TareaServiceException.class)
  public void crearNuevoUsuarioLoginRepetidoLanzaExcepcion(){
     TareaService tareaService = newTareaService();
     List<Tarea> tareas = tareaService.allTareasUsuario(1023301L);
  }

  // Test #21: nuevaTareaUsuario
  @Test
  public void nuevaTareaUsuario() throws ParseException {
     TareaService tareaService = newTareaService();
     long idUsuario = 1000L;
     long idTablero = 1000L;
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     tareaService.nuevaTarea(idUsuario, "Pagar el alquiler", sdf.parse("2017-12-01"), idTablero);
     assertEquals(4, tareaService.allTareasUsuario(1000L).size());
  }

  // Test #22: modificación de tareas
  @Test
  public void modificacionTarea() throws ParseException {
     TareaService tareaService = newTareaService();
     long idTarea = 1000L;
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     tareaService.modificaTarea(idTarea, "Pagar el alquiler", sdf.parse("2017-12-01"));
     Tarea tarea = tareaService.obtenerTarea(idTarea);
     assertEquals("Pagar el alquiler", tarea.getTitulo());
  }

  // Test #23: borrado tarea
  @Test
  public void borradoTarea() {
     TareaService tareaService = newTareaService();
     long idTarea = 1000L;
     tareaService.borraTarea(idTarea);
     assertNull(tareaService.obtenerTarea(idTarea));
  }

  // Test #46: cambiaTerminada
  @Test
  public void cambiaTerminada() {
     TareaService tareaService = newTareaService();
     long idTarea = 1000L;
     Tarea tareaDevuelta = tareaService.cambiaTerminada(idTarea);
     assertEquals(tareaDevuelta, tareaService.obtenerTarea(idTarea));
     assertTrue(tareaService.obtenerTarea(idTarea).getTerminada());
  }

  // Test #47: cambiaTerminadaDosVeces
  @Test
  public void cambiaTerminadaDosVeces() {
     TareaService tareaService = newTareaService();
     long idTarea = 1000L;
     Tarea tareaDevuelta = tareaService.cambiaTerminada(idTarea);
     assertEquals(tareaDevuelta, tareaService.obtenerTarea(idTarea));
     assertTrue(tareaService.obtenerTarea(idTarea).getTerminada());
     tareaDevuelta = tareaService.cambiaTerminada(idTarea);
     assertEquals(tareaDevuelta, tareaService.obtenerTarea(idTarea));
     assertFalse(tareaService.obtenerTarea(idTarea).getTerminada());
  }

  // Test #49: comprobacionFechaCreacion
  // Comprobamos solo la fecha (no la hora) ya que, al ser segundos es imposible acertar exactamente
  @Test
  public void comprobacionFechaCreacion() throws ParseException {
     TareaService tareaService = newTareaService();
     long idUsuario = 1000L;
     long idTablero = 1000L;
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

     tareaService.nuevaTarea(idUsuario, "Comprobar fecha", sdf.parse("2017-12-01"), idTablero);
     List<Tarea> tareas = tareaService.allTareasUsuario(1000L);

     int year_fechaCreacion = tareas.get(0).getFechaCreacion().getYear();
     int month_fechaCreacion = tareas.get(0).getFechaCreacion().getMonth();
     int day_fechaCreacion = tareas.get(0).getFechaCreacion().getDate();
     Date hoy_fechaCreacion = new Date(year_fechaCreacion, month_fechaCreacion, day_fechaCreacion);
     Date hoy = new Date();
     assertEquals(sdf.format(hoy), sdf.format(hoy_fechaCreacion));
  }

  // Test #51: modificacionFechaLimite
  @Test
  public void modificacionFechaLimite() throws ParseException {
     TareaService tareaService = newTareaService();
     long idTarea = 1000L;
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     tareaService.modificaTarea(idTarea, "Pagar el alquiler", sdf.parse("2017-12-31"));
     Tarea tarea = tareaService.obtenerTarea(idTarea);
     assertEquals(sdf.parse("2017-12-31"), tarea.getFechaLimite());
  }

  // Test #60: nuevaTareaTablero
  @Test
  public void nuevaTareaTablero() throws ParseException {
     TareaService tareaService = newTareaService();
     long idUsuario = 1000L;
     long idTablero = 1000L;
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     tareaService.nuevaTarea(idUsuario, "Comprobar fecha", sdf.parse("2017-12-01"), idTablero);
     List<Tarea> tareas = tareaService.allTareasUsuario(1000L);

     assertEquals("Tablero test 1", tareas.get(0).getTablero().getNombre());
  }

}
