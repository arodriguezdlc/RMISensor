import java.rmi.*;
import java.util.*;

interface ServicioAlarmas extends Remote {
	void alta(Sensor s) throws RemoteException;
	void baja(Sensor s) throws RemoteException;
	void enviaAlarma() throws RemoteException;	
}