
# Informe

## Historias de usuario escogidas para el sprint.

* Tareas de: Quico Llinares Llorens

  * [SGT-9 Gestionar grupos](https://github.com/mads-ua/todolistgrupo-2017-equipo-14/wiki/SGT-9-Gestionar-grupos)

**Descripción**

Como usuario quiero poder gestionar grupos.


**COS - Condiciones de Satisfacción**

* Crear la entidad grupo, la cual tendrá un administrador y unos miembros (usuarios).
* Crear funciones para crear, eliminar y editar grupos.
* Añadir botones para ver mis grupos (navbar).
* Dentro de ver mis grupos, tendré botones para eliminarlos, crear nuevos o editarlos (añadiendo o borrando miembros).


**Funcionalidades implementadas**

Se ha creado la entidad grupo, la cual nos permite formar conjunto de usuarios (un administrador y los miembros que desees). Se han creado también todos los métodos que permiten gestionar esta nueva entidad como son crear, editar, borrar y listar. Además, el administrador puede agregar participantes al grupo creado por él mismo, mientras que los participantes de un grupo no podrán editar ni borrar el grupo.



  * [SGT-11 Asignar tamaños a los tableros](https://github.com/mads-ua/todolistgrupo-2017-equipo-14/wiki/SGT-11-Asignar-tama%C3%B1o-a-los-tableros)

**Descripción**

Como administrador de un tablero, quiero poder añadir tamaños para compartirlos con mis compañeros.


**COS - Condiciones de Satisfacción**

* Cambiar la entidad tablero, para que tenga tamaños.
* Funciones en TableroService para añadir tamaños a un tablero y borrarlos.
* Añadir en las tablas de tareas el tamaño de la tarea (modificando el modelo de Tarea).
* Añadir botón para gestionar tamaños.
* Crear vista para la edición de tamaños.
* Cambiar vistas crear y editar tarea, para que al seleccionar un tablero salgan sus tamaños.


**Funcionalidades implementadas**

Se ha agregado el atributo tamaños a la entidad Tablero, permitiendo así que, un usuario de un tablero, pueda crear diferentes tamaños que asignar a las tareas asignadas a ese tablero. Se han creado también todos los métodos que permiten gestionar este nuevo atributo como son crear, editar, borrar y listar.




* Tareas de: Eduardo Navarro Poveda

  * [SGT-8 Tareas en tableros](https://github.com/mads-ua/todolistgrupo-2017-equipo-14/wiki/SGT-8-Tareas-en-tableros)

**Descripción**

Como usuario quiero crear tareas que se asignen a un tablero determinado.


**COS - Condiciones de Satisfacción**

* Posibilidad de asignar una tarea a un tablero.
* Ver todas las tareas asignadas a un determinado tablero.


**Funcionalidades implementadas**

Se ha agregado la relación entre las entidades Tarea y Tablero que permiten asignar las tareas que deseen a un tablero en concreto. Se ha creado también los métodos crear y listar de gestión, mientras que los métodos de edición y borrado se han pospuesto para futuras versiones en las que se asigne la tarea Gestión de tableros.


  * [SGT-12 Comentarios tarea](https://github.com/mads-ua/todolistgrupo-2017-equipo-14/wiki/SGT-12-Comentarios-tarea)

**Descripción**

Como usuario quiero crear comentarios en las tareas, para comunicarme con mis compañeros.


**COS - Condiciones de Satisfacción**

* Posibilidad de crear un comentario en una tarea.
* Posibilidad de editar un comentario en una tarea.


**Funcionalidades implementadas**

Se ha creado la entidad Comentario que permite a los usuarios agregar un comentario en una Tarea para poder comunicarse con el resto de usuarios. Se han creado también todos los métodos que permiten gestionar este nuevo atributo como son crear, editar, borrar y listar. Un usuario podrá ver y/o agregar un comentario en una Tarea creada por él mismo, o en una Tarea que esté en un Tablero en él que el participe o administre. Un usuario no podrá modificar ni borrar comentarios hechos por otros usuarios.



* Tareas de: David Pérez Segura
  * [SGT-10 Mostrar tareas en calendario](https://github.com/mads-ua/todolistgrupo-2017-equipo-14/wiki/SGT-10-Mostrar-tareas-en-calendario)

**Descripción**

Como usuario quiero poder ver mis tareas en un calendario.


**COS - Condiciones de Satisfacción**

* Crear la vista del calendario
* Ver las tareas en el calendario
* Añadir boton para ver mi calendario (navbar).


**Funcionalidades implementadas**

Se ha creado la entidad Calendario que permite a los usuarios ver las Tareas por su fecha límite de entrega dentro de un Calendario.



## Informe sobre la metodología seguida.

Hemos utilizado la metodología Scrum que es un proceso en el que se aplican de manera regular un conjunto de prácticas para trabajar colaborativamente, en equipo, y obtener el mejor resultado posible de un proyecto.

Gracias a que hemos empleado esta metodología y a que hemos hecho los daily meeting (semanales en nuestro caso) hemos podido planificar nuestro trabajo hecho y por hacer y hemos ido subiendo paulatinamente commits cada semana.

Ahora mostraremos un par de gráficas para ver como ha ido evolucionando nuestro proyecto a lo largo del mes.


![Grafica 2](https://image.ibb.co/i3vwPm/grafica2.png)

En la gráfica anterior, podemos observar los commits realizados por el conjunto del equipo durante este mes dado en la práctica 4. Como podemos ver, todas las semanas hemos realizado varios commits y hemos intentado que no hubieran picos muy altos, que todas las semanas tuvieran un número similar de commits.


![Grafica 1](https://image.ibb.co/gHaKc6/grafica1.png)

En esta gráfica, están los commits realizados durante este mes por nuestro grupo. La gráfica muestra que tanto Edu como Quico han tenido un número de commits muy similar y, aunque el número de commits sea menor en el caso de David, hay que añadir que la tarea encargada a este miembro requería mucha búsqueda de información, la cuál no se puede mostrar en esta gráfica, pero se ha de tener en cuenta.

Teniendo esa información en mente, podemos afirmar que se ha repartido de forma equitativa el trabajo entre los miembros del equipo y que han realizado un esfuerzo similar en la realización de esta práctica.



## Informes sobre las reuniones de Scrum.

* **Daily meeting 1**

  * Quico Llinares Llorens

En esta primera semana, he creado:

- El modelo de grupos, relacionándolo con los usuarios (administradores y miembros).
- Su Interfaz del repositorio y JPARepository.
- Los respectivos tests de los métodos del JPARepository, actualizando el esquema sql y el dataset.

En esta próxima semana realizaré las siguientes tareas:

- Añadir al repository el método para borrar grupos.
- Crear métodos de servicio a partir de los del repository.
- Crear vistas con acciones.

No he tenido ningún problema a destacar, el desarrollo ha sido ininterrumpido


 * Eduardo Navarro Poveda

En esta primera semana, he realizado la parte funcional de tareas en tableros:

- He creado la relación entre la clase Tarea y Tablero
- He hecho que, al crear una tarea, se le pueda (opcionalmente) asignar un tablero.
- He hecho que, al modificar una tarea, se le pueda (opcionalmente) asignar un tablero.

También he empezado la parte visual del mismo:

- Al añadir una tarea, he añadido un desplegable en el que salen todos los tableros del usuario para poder seleccionar (o no) uno de ellos y, así, asignarla a la nueva Tarea.
- He añadido la fila Tablero en la vista de tareas para ver el Tablero que tiene asignado la Tarea.

Ya que lo realizado ha sido muy similar a cosas ya hechas en las prácticas anteriores, no he encontrado ningún problema remarcable.

En la semana siguiente, realizaré las siguientes tareas:

- Al modificar una tarea, añadir un desplegable en el que saldrán todos los tableros del usuario para poder seleccionar (o no) uno de ellos.
- Poder ver las Tareas asignadas a un Tablero, en la vista del Tablero concreto.


 * David Pérez Segura

En esta primera semana, he creado:

- Una vista del calendario.
- Un calendario básico realizado con JavaScript que permite cambiar de mes y mostrar la fecha actual. Pero todavía no muestra las tareas del usuario.

En esta próxima semana realizaré las siguientes tareas:

- Una llamada que me permita recibir las tareas del usuario comprendidas en un rango de fechas, de ese modo reducimos el uso del servidor y trabajamos de manera más eficiente.

No he tenido ningún problema de carácter importante, sólo que he tenido que utilizar un lenguaje que no se había empleado anteriormente en la práctica, JavaScript.



* **Daily meeting 2**

  * Quico Llinares Llorens

En esta segunda semana, he creado:

- El método para borrar grupos en el JPA con tests.
- La clase GrupoService.
- Las vistas, el controlador y he añadido en la barra de navegación los grupos.

En esta próxima semana realizaré las siguientes tareas:

- Crear modelo para el tamaño de las tareas (cambiar modelo de tareas y tableros).
- Crear Repository para size.
- Crear funciones de servicio en TableroService.

No he tenido ningún problema a destacar, el desarrollo ha sido ininterrumpido.


 * Eduardo Navarro Poveda

En esta segunda semana, he realizado la parte visual de tareas en tableros y he terminado la historia SGT-8 Tareas en tableros:

- Al modificar una tarea, añadir un desplegable en el que saldrán todos los tableros del usuario para poder seleccionar (o no) uno de ellos.
- Poder ver las Tareas asignadas a un Tablero, en la vista del Tablero concreto.

Ya que no hemos cogido la tarea de gestión de Tableros, no ha hecho falta crear una tabla en detalles de Tablero que nos permitiese gestionar la Tarea de un Tablero concreto. El resto ha sido similar a lo ya realizado por lo que no he encontrado ningún error remarcable.

En la semana siguiente, empezaré la historia SGT-12 Comentarios en tareas y realizaré las siguientes tareas:

- Crear clase Comentario.
- Crear la relación entre la clase Tarea y Comentario.
- Permitir agregar comentarios a una Tarea determinada.


 * David Pérez Segura

En esta segunda semana he realizado la parte de backend del servidor que permite devolver en formato JSON las tareas comprendidas en el mes que el calendario, de ese modo no devolvemos todas las tareas del usuario que ha iniciado sesión y se trabaja de manera más eficiente.
Del mismo modo, he añadido dichas tareas al calendario, mostrando los días que contienen tareas en un color distinto.

En la semana siguiente, realizaré un desplegable al pasar el cursor por encima de la celda que tiene tareas para visualizar la información acerca de dicha tarea, por ejemplo, el título.

He tenido diversos problemas con la implementación de la parte de javascript empleando ajax, ya que en el controlador surgía el problema de la recursión infinita, un usuario tiene tareas, y una tarea tiene un usuario, por lo tanto se convierte en un bucle infinito, finalmente lo he solucionado creando una tarea auxiliar y pasando los datos que interesaba obtener.


## Incidencias remarcables

Nuestro proyecto ha tenido un problema remarcable y es que, la imagen docker del proyecto no se construye bien. Algunos de los elementos visuales de la página no se muestran como deberían, y el funcionamiento es distinto al que debería.

Otra incidencia a remarcar es que tuvimos un error al subir manualmente la versión 1.2 de nuestro proyecto a docker y se subió una versión anterior. Esto está corregido en la versión **1.2.1**.

Por lo demás, no ha habido ningún problema. Hemos podido realizar el trabajo al que nos comprometimos semanalmente gracias a la contínua comunicación del grupo y a los daily meetings.


## Resultado de la retrospectiva: qué ha ido bien y qué se podría mejorar.

En general ha ido todo perfectamente. Hemos realizado commits e issues progresivamente, sin dejar semanas vacías y hemos terminado a tiempo y satisfactoriamente el trabajo asignado a cada miembro del equipo.

Sin embargo, han habido ciertos bugs en algunos issues cerrados que ha habido que reabrir para arreglarlos. Estos errores han sido debidos a ciertos malentendidos en las tareas, por lo que, igual tendríamos que mejorar en la descripción de las tareas, describiendo todos los detalles de forma mas exhaustiva para no tener estos malentendidos en el futuro.
