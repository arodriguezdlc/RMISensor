import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

import java.rmi.RemoteException;
import java.util.*;

public class Monitor extends Thread {
	/** 
	 * Esta clase se encarga de monitorizar el uso de CPU, RAM y Disco y lo almacena en variables accesibles
	 * mediante los metodos get correspondientes. Utiliza librerias del sistema para obtener esos valores.
	 */

	/* Parametros */
	volatile private Long cpu;
	volatile private Long ram;
	//volatile private Long disk;
	private Integer tiempoMuestreo; //Para asignar un tiempo de muestreo al monitor en ms.	
	private boolean threadRunFlag;
	private List<Alarma> listaAlarmas;
	private ServicioAlarmas srv;

	/* Constructor */
	Monitor(Integer tiempoMuestreo, ServicioAlarmas srv) {
		this.setTiempoMuestreo(tiempoMuestreo);
		this.srv = srv;
		this.start();    // Se inicia el hilo al crear el objeto.
	}

	/* Metodos */
	synchronized public Long getCPU() {
		Long cpu = Long.valueOf(0);
		try {
			wait();
			cpu = this.cpu;
			notify();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return cpu;
	}
	synchronized private void setCPU(Long cpu) {
		try {
			wait();
			this.cpu = cpu;
			notify();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	synchronized public Long getRam() {
		Long ram = Long.valueOf(0);
		try {
			wait();
			ram = this.ram;
			notify();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return ram;
	}
	synchronized private void setRam(Long ram) {
		try {
			wait();
			this.ram = ram;
			notify();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*public Long getDisk() {
		return this.disk;
	}*/
	public Integer getTiempoMuestreo() {
		return this.tiempoMuestreo;
	}
	private void setTiempoMuestreo(Integer tiempoMuestreo) {
		this.tiempoMuestreo = tiempoMuestreo;
	}
	synchronized public void setListaAlarmas(List<Alarma> listaAlarmas) {
		this.listaAlarmas = listaAlarmas;
	}

	@Override
	public void run() throws RemoteException {
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

				this.compruebaAlarmas();
				try {
					Thread.sleep(this.tiempoMuestreo);
				} catch (InterruptedException e) {}
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

	private void compruebaAlarmas() throws RemoteException {
		List<Alarma> alarmasActivadas = new LinkedList<Alarma>();
		for (Alarma a: listaAlarmas) {
			if(a.getParametro().equals("CPU")) {
				if(a.getEsMayorQueUmbral()) {
					if(a.getUmbral() < this.getCPU()) {
						alarmasActivadas.add(a);
					}
				} else {
					if(a.getUmbral() > this.getCPU()) {
						alarmasActivadas.add(a);
					}
				}
			} else if (a.getParametro().equals("RAM")) {
				if(a.getEsMayorQueUmbral()) {
					if(a.getUmbral() < this.getRam()) {
						alarmasActivadas.add(a);
					}
				} else {
					if(a.getUmbral() > this.getRam()) {
						alarmasActivadas.add(a);
					}
				}
			/*} else if (a.getParametro().equals("DISK")) {
				if(a.getEsMayorQueUmbral()) {
					if(a.getUmbral() < this.getDisk()) {
						alarmasActivadas.add(a);
					}
				} else {
					if(a.getUmbral() > this.getDisk()) {
						alarmasActivadas.add(a);
					}
				}*/
			} /*else {
				//throw UnexpectedException;

			}*/
		}
		//Enviamos lista de alarmas
		if(!alarmasActivadas.isEmpty()) {
			srv.enviaListaAlarmas(alarmasActivadas);
		}
	}
}