import java.rmi.*;
import java.util.*;

interface Sensor extends Remote {
	
	void crearAlarma(Alarma alarma) throws RemoteException;
	void eliminarAlarma(Alarma alarma) throws RemoteException;
	String getSensorName() throws RemoteException;
	List<Alarma> getListaAlarmas() throws RemoteException;
}