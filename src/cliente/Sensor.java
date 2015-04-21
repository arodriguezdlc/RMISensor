import java.rmi.*;

interface Sensor extends Remote {
	
	public void setAlarma(Alarma alarma);
	public Alarma getListaAlarmas();
}