import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.util.logging.*;

public class Monitor extends Thread {
	/** 
	 * Esta clase se encarga de monitorizar el uso de CPU, RAM y Disco y lo almacena en variables accesibles
	 * mediante los metodos get correspondientes. Utiliza librerias del sistema para obtener esos valores.
	 */

	private static final Logger logger =
			Logger.getLogger(Monitor.class.getName());
	
	/* Parametros */
	volatile private Long cpu;
	volatile private Long ram;
	//volatile private Long disk;
	private Integer tiempoMuestreo; //Para asignar un tiempo de muestreo al monitor en ms.	
	private boolean threadRunFlag;
	/* Constructor */

	Monitor(Integer tiempoMuestreo) {
		this.setTiempoMuestreo(tiempoMuestreo);
	}

	/* Metodos */
	synchronized public Long getCPU() {
		return this.cpu;
	}
	synchronized private void setCPU(Long cpu) {
		this.cpu = cpu;
	}
	synchronized public Long getRam() {
		return this.ram;
	}
	synchronized private void setRam(Long ram) {
		this.ram = ram;
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
		try {
			Long cpu;
			this.threadRunFlag = true;

			OperatingSystemMXBean bean =
					(com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

			while (this.threadRunFlag) {
				if (0 > (cpu = Double.valueOf(bean.getSystemCpuLoad()).longValue())) {
					this.setCPU(cpu * 100);
				}
				this.setRam(this.getPorcentajeMemoria(bean.getFreePhysicalMemorySize(),
						bean.getTotalPhysicalMemorySize()));
				//TODO disk
				try {
					Thread.sleep(this.tiempoMuestreo);
				} catch (InterruptedException e) {
				}
			}
			this.join();
		} catch (InterruptedException e) {
			System.err.println("HILO INTERRUMPIDO\n");
			e.printStackTrace();
			System.exit(1);
		}
	}
	public void stopThread() {
		/**
		 * Pone a false la bandera threadRunFlag, que es la que permite la ejecución del bucle del hilo
		 * Al ponerse a false se sale del hilo.
		 */
		this.threadRunFlag = false;
	}

	private Long getPorcentajeMemoria(Long memoriaLibre, Long memoriaTotal) {
		return ((1-memoriaLibre)/memoriaTotal) * 100;
	}
}