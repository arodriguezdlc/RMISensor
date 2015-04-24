import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

public class SensorImpl extends UnicastRemoteObject implements Sensor {

	/* Atributos */
	List<Alarma> l;
	Monitor m;

	/* Constructores */
	SensorImpl(Integer tiempoMuestreo, ServicioAlarmas srv) throws RemoteException {
		l = new LinkedList<Alarma>();
		m = new Monitor(tiempoMuestreo, srv, l);
	}
	/* Metodos */
	@Override
	public void crearAlarma(Alarma alarma) {
		l.add(alarma);
		m.setListaAlarmas(l); //Actualizamos lista de alarmas del monitor
	}
	public void eliminarAlarma(Alarma alarma) {
		l.remove(l.indexOf(alarma));
		m.setListaAlarmas(l); //Actualizamos lista de alarmas del monitor
	}
	@Override
	public List<Alarma>getListaAlarmas() {
		return this.l;
	}
}