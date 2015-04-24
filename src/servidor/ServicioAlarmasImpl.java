import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

class ServicioAlarmasImpl extends UnicastRemoteObject implements ServicioAlarmas {
	List<Sensor> l;

	ServicioAlarmasImpl() throws RemoteException {
		l = new LinkedList<Sensor>();
	}

	public void alta(Sensor s) throws RemoteException {
		l.add(s);
	}

	public void baja(Sensor s) throws RemoteException {
		l.remove(l.indexOf(s));
	}

	public void enviaListaAlarmas(List<Alarma> listaAlarmasActivadas) throws RemoteException {
		for(Alarma a : listaAlarmasActivadas) {
			System.out.println(a.toString());
		}
	}
}