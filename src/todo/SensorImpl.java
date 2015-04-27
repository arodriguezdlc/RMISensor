import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

public class SensorImpl extends UnicastRemoteObject implements Sensor {

	/* Atributos */
	List<Alarma> l;
	Monitor m;
	String sensorName;

	/* Constructores */
	SensorImpl(Integer tiempoMuestreo, ServicioAlarmas srv, String sensorName) throws RemoteException {
		l = new LinkedList<Alarma>();
		m = new Monitor(tiempoMuestreo, srv, l);
		this.sensorName = sensorName;
	}
	/* Metodos */
	@Override
	public void crearAlarma(Alarma alarma) throws RemoteException {
		l.add(alarma);
		m.setListaAlarmas(l); //Actualizamos lista de alarmas del monitor
		System.out.println("Creada nueva alarma");
	}
	public void eliminarAlarma(Alarma alarma) throws RemoteException {
		l.remove(l.indexOf(alarma));
		m.setListaAlarmas(l); //Actualizamos lista de alarmas del monitor
	}
	public String getSensorName() throws RemoteException {
		return this.sensorName;
	}
	@Override
	public List<Alarma> getListaAlarmas() throws RemoteException {
		return this.l;
	}
	public void apagaMonitor() {
		m.stopThread();
	}
}