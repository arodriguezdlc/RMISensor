package cliente;

import java.util.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class Monitor extends Thread {
	/** 
	 * Esta clase se encarga de monitorizar el uso de CPU, RAM y Disco y lo almacena en variables accesibles
	 * mediante los metodos get correspondientes. Utiliza librerias del sistema para obtener esos valores.
	 */
	
	
	/* Parametros */
	private Long cpu;
	private Long ram;
	//private Long disk;
	private Integer tiempoMuestreo; //Para asignar un tiempo de muestreo al monitor en ms.	

	/* Constructor */

	Monitor(Integer tiempoMuestreo) {
		this.setTiempoMuestreo(tiempoMuestreo);
	}

	/* Metodos */
	public Long getCPU() {
		return this.cpu;
	}
	public Long getRam() {
		return this.ram;
	}
	/*public Long getDisk() {
		return this.disk;
	}*/
	public Integer getTiempoMuestreo() {
		return this.tiempoMuestreo;
	}
	public void setTiempoMuestreo(Integer tiempoMuestreo) {
		this.tiempoMuestreo = tiempoMuestreo;
	}

	@Override
	public void run() {
		/**
			Metodo run que se ejecuta en otro hilo. Se encarga de obtener los valores de CPU y RAM del PC
			y almacenarlo en las variables de la clase.
		*/
		Long cpu;

		OperatingSystemMXBean bean = 
			(com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
		
		while(true) {
			if ( 0 > (cpu = Double.valueOf(bean.getSystemCpuLoad().longValue))) {
				this.cpu = cpu*100;	//Deberia ser protegido con un semaforo.
			}
			this.ram = this.getPorcentajeMemoria(bean.getFreePhysicalMemorySize(), 
				bean.getTotalPhysicalMemorySize());
			//TODO disk
			Thread.sleep(this.tiempoMuestreo);
		}
	}

	private Long getPorcentajeMemoria(Long memoriaLibre, Long memoriaTotal) {
		return ((1-memoriaLibre)/memoriaTotal) * 100;
	}
}