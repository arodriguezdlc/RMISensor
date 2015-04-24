import java.io.*;
import java.util.Date;

public class Alarma implements Serializable{

	/* Atributos */
	private String titulo;		//Solo puede ser editado en la creacion del objeto
	private String descripcion;	//Solo puede ser editado en la creacion del objeto
	private String parametro;	//Solo puede ser editado en la creacion del objeto.
	private Integer umbral;	//Solo puede editado en la creacion del objeto.
	private Boolean esMayorQueUmbral; //Solo puede ser editado en la creación del objeto.
	private Date fecha;	//Editado por el cliente cuando se produce la alarma.
	private String prioridad; //Solo puede ser editado en la creacion del objeto.

	/* Constructores */
	public void Alarma(String titulo, String descripcion, String parametro, 
		Integer umbral, Boolean esMayorQueUmbral, String prioridad) {
		this.titulo = titulo;
		this.descripcion = descripcion;		
		this.prioridad = prioridad;
		this.parametro = parametro;
		this.umbral = umbral;
		this.esMayorQueUmbral = esMayorQueUmbral;

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
	public Integer getUmbral() {
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
		return fecha + "  -> ¡ALARMA " + prioridad + "!\n" + "\tTitulo: " + titulo +
			"\n\tDescripcion:" + descripcion;
	}
}