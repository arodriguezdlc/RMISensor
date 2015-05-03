# RMI SENSOR

## Introducción

RMI Sensor es un software de monitorización y gestión de alarmas para equipos basado en Java y en la tecnología RMI.

Su funcionalidad principal es la de recibir alarmas creadas por el usuario de forma centralizada de distintos equipos. Para ello lo único que hay que hacer es ejecutar el cliente de la aplicación en los sistemas que se quieren monitorizar, y desde la interfaz del servidor se realizan las configuraciones que sean necesarias.

## Arquitectura

La arquitectura de la aplicación está basada en el modelo cliente-servidor. Los clientes son los sensores que recopilan datos del equipo y si ocurre una alarma, notifican al servidor, que el equipo central desde el que se controla la aplicación, y que implementa una interfaz de usuario.

 ![unnamed](/Users/fernandodominguez/Documents/4º Teleco/Sistemas Distribuidos/TrabajoRMI/unnamed.png)





## Funcionamiento RMI

### Servidor

La comunicación RMI la inician los sensores cuando comienza su ejecución. Por lo tanto el servidor debe estar registrado en el _RMIRegistry_ para el correcto funcionamiento de la aplicación. En esa primera comunicación, los sensores se registran en el servidor, el cual tiene una lista enlazada con las referencias de los sensores.

La interfaz del servicio es la siguiente:

``` java
interface ServicioAlarmas extends Remote {
	void alta(Sensor s) throws RemoteException;
	void baja(Sensor s) throws RemoteException;
	void enviaListaAlarmas(List<Alarma> listaAlarmasActivadas) throws RemoteException;
}
```

La interfaz tiene 3 métodos:

- **alta**: método para registrar el sensor en el servidor.
- **baja**: método para eliminar el registro del sensor en el servidor.
- **enviaListaAlarmas**: método para enviar las alarmas que se han activado en cierto momento.

El servidor por otro lado arranca el servicio y muestra al usuario un menú para que decida lo que desea hacer. Las opciones de este menú son:

- Listado de los sensores actualmente registrados contra el servidor.
- Creación de una nueva alarma en uno de los sensores actualmente registrados.
- Listado de las alarmas activas.
- Apagar el servidor.

### Sensores

Los clientes se basan en distintos componentes: un cliente para la comunicación con el servidor y un conjunto de sensores y monitores para la recopilación de datos.

#### Cliente

El cliente se encarga simplemente de instanciar un objeto representativo del servidor para posteriormente dar de alta una nueva alarma que estará en funcionamiento hasta que el usuario lo decida.

`Cliente.java`:

``` java
static public void main (String args[]) {
  [...]
  ServicioAlarmas srv = (ServicioAlarmas) 
    Naming.lookup("//" + args[0] + ":" + args[1] + "/RMISensor");
  /* Creamos un nuevo sensor */
  SensorImpl s = new SensorImpl(Integer.parseInt(args[3]), srv, args[2]);
  /* Lo damos de alta */
  srv.alta(s);
  /* El sensor estará funcionando hasta que se pulse Enter */
  System.out.println("Sensor activado, pulse enter para desactivar");
  Scanner input = new Scanner(System.in);
  input.nextLine();
  /* Lo damos de baja y terminamos la ejecución */
  srv.baja(s);
  s.apagaMonitor();
  System.exit(0);
}
```

#### Monitorización

La monitorización se realiza en las clases `Monitor.java` y `Sensor.java`. `Monitor.java` realiza la monitorización de CPU y memoria RAM del propio sistema mientras que `Sensor.java` maneja esta clase, las alarmas y las estructuras de datos necesarias para el funcionamiento del sistema.

Los sensores tienen también una interfaz para que el servidor pueda configurar las alarmas que pueden activarse en cada sensor:

``` java
interface Sensor extends Remote {
	
	void crearAlarma(Alarma alarma) throws RemoteException;
	void eliminarAlarma(Alarma alarma) throws RemoteException;
	String getSensorName() throws RemoteException;
	List<Alarma> getListaAlarmas() throws RemoteException;
}
```

La interfaz tiene 4 métodos:

- **crearAlarma**: para configurar una alarma en el sensor. Se le pasa un objeto alarma que es creado en el servidor. Ese objeto debe ser serializable para que pueda ser enviado a los sensores. Se explicará más adelante.
- **eliminarAlarma**: método para eliminar una alarma ya configurada.
- **getSensorName**: al iniciar un sensor, uno de los parámetros es el sensorName. Este método devuelve el sensorName de dicho sensor.
- **getListaAlarmas**: devuelve la lista de alarmas que están activas registradas en el sensor.

Los sensores manejan un objeto `Monitor` que obtiene los valores de carga de CPU y de uso de memoria RAM del equipo donde se ejecuta y lo almacena en variables accesibles a través de métodos get. La recolección de datos se hace en un hilo independiente para no bloquear al resto del programa. Esta misma clase se encarga de ver las alarmas que tiene activadas el sensor y en el caso de que alguna salte, llama al método `enviaListaAlarmas` del servidor.

El siguiente pseudo-código describe la ejecución de `Monitor`:

``` java
public void run(){
	getCpuUsage();
  	getRamUsage();
  	for (Alarma a in listaAlarmas){
      if (alarma.superaUmbral()){
      	alarma.activaAlarma();
      }
  	}
  	Thread.sleep(tiempoMuestreo);
}
```



## Detalle de ejecución

Para la correcta ejecución del programa debemos seguir estos pasos:

- Ejecución de `rmiregistry puerto` en el directorio y puerto que se vaya a utilizar para el servidor.
  
- Ejecución del servidor mediante la orden: `java -Djava.security.policy=servidor.permisos Servidor puerto`
  
  - Una vez incializado el servidor se podrán tomar las acciones pertinentes mediante su interfaz.
  
- Ejecución de los clientes mediante el comando: `java -Djava.security.policy=cliente.permisos Cliente host puerto nombreSensor tiempoMuestreo`
  

Una combinación válida de parámetros podría ser:

- `puerto`: 55555
- `host`: localhost
- `nombreSensor`: sensor1
- `tiempoMuestreo`: 1000

## Conclusión

