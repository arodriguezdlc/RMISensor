import java.rmi.*;
import java.util.*;

interface Sensor extends Remote {
	
	void crearAlarma(Alarma alarma);
	void eliminarAlarma(Alarma alarma);
	String getSensorName();
	List<Alarma> getListaAlarmas();
}