import java.rmi.*;
import java.util.*;

interface ServicioAlarmas extends Remote {
	void alta() throws RemoteException;
	void baja() throws RemoteException;
	void enviaAlarma() throws RemoteException;	
}