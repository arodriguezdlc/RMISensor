import java.io.*;
import java.lang.String;
import java.lang.System;
import java.rmi.RemoteException;
import java.util.Date;

public class Alarma implements Serializable {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[";

	/* Atributos */
	private String titulo;		//Solo puede ser editado en la creacion del objeto
	private String descripcion;	//Solo puede ser editado en la creacion del objeto
	private String parametro;	//Solo puede ser editado en la creacion del objeto.
	private Double umbral;	//Solo puede editado en la creacion del objeto.
	private Boolean esMayorQueUmbral; //Solo puede ser editado en la creación del objeto.
	private Date fecha;	//Editado por el cliente cuando se produce la alarma.
	private String prioridad; //Solo puede ser editado en la creacion del objeto.
	private Sensor sensor;

	/* Constructores */
	public Alarma(String titulo, String descripcion, String parametro,
		Double umbral, Boolean esMayorQueUmbral, String prioridad, Sensor s) {
		this.titulo = titulo;
		this.descripcion = descripcion;		
		this.prioridad = prioridad;
		this.parametro = parametro;
		this.umbral = umbral;
		this.esMayorQueUmbral = esMayorQueUmbral;
		this.sensor = s;

	}

	/* Metodos */
	public String getTitulo() {
		return this.titulo;
	}	
	public String getDescripcion() {
		return this.descripcion;
	}
	public String getParametro() {
		return this.parametro;
	}
	public Double getUmbral() {
		return this.umbral;
	}
	public Boolean getEsMayorQueUmbral() {
		return this.esMayorQueUmbral;
	}
	public Date getFecha() {
		return this.fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getPrioridad() {
		return this.prioridad;
	}

	public String toString() {

		String msg = "ALARMA!";

		try {

			switch (prioridad.toLowerCase()) {
				case "alta":
					msg = ANSI_RED + fecha + "  -> ALARMA " + prioridad + ANSI_RESET + "\n";
					break;
				case "media":
					msg = ANSI_YELLOW + fecha + "  -> ALARMA " + prioridad + ANSI_RESET + "\n";
					break;
				case "baja":
					msg = ANSI_GREEN + fecha + "  -> ALARMA " + prioridad + ANSI_RESET + "\n";
					break;
			}
			msg = msg +
					"\tSensor: " + sensor.getSensorName() + "\n" +
					"\tTitulo: " + titulo + "\n" +
					"\tDescripcion:" + descripcion;

		}catch (RemoteException e){
			System.out.println("Remote exception");
		}

		return msg;
	}
}