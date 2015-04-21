import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

public class SensorImpl extends UnicastRemoteObject implements Sensor {

	/* Atributos */
	List<Alarma> l;

	/* Constructores */
	SensorImpl() throws RemoteException {
		l = new LinkedList<Alarma>();
	}

	



}