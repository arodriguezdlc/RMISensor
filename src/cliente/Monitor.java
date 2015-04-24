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
	private double cpu;
	private long ram;
	//volatile private Long disk;
	private Integer tiempoMuestreo = new Integer(0); //Para asignar un tiempo de muestreo al monitor en ms.
	private boolean threadRunFlag;
	private List<Alarma> listaAlarmas;
	private ServicioAlarmas srv;

	/* Constructor */
	Monitor(Integer tiempoMuestreo, ServicioAlarmas srv, List<Alarma> listaAlarmas) {
		this.setTiempoMuestreo(tiempoMuestreo);
		this.srv = srv;
		this.setListaAlarmas(listaAlarmas);
		this.start();   // Se inicia el hilo al crear el objeto.
	}
	Monitor(Integer tiempoMuestreo, List<Alarma> listaAlarmas) {
		this.setTiempoMuestreo(tiempoMuestreo);
		this.srv = null;
		this.setListaAlarmas(listaAlarmas);
		this.start();
	}

	/* Metodos */
 	synchronized public double getCPU() {
		return this.cpu;
	}
	synchronized private void setCPU(double cpu) {
		this.cpu = cpu;
	}
	synchronized public long getRam() {
		return this.ram;
	}
	synchronized private void setRam(long ram) {
		this.ram = ram;
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
	public void run() {
		/**
			Metodo run que se ejecuta en otro hilo. Se encarga de obtener los valores de CPU y RAM del PC
			y almacenarlo en las variables de la clase.
		*/
		try {
			Double cpu;
			Long ram;
			this.threadRunFlag = true;

			OperatingSystemMXBean bean =
					(com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

			while (this.threadRunFlag) {
				/*if (0 >= (*/cpu = bean.getSystemCpuLoad()/*)) {*/;
					this.setCPU(cpu);
				//}
				this.setRam(this.getPorcentajeMemoria(bean.getFreePhysicalMemorySize(),
						bean.getTotalPhysicalMemorySize()));
				//TODO disk


				try {
					if(!this.listaAlarmas.isEmpty()) {
						this.compruebaAlarmas();
					}
					Thread.sleep(this.tiempoMuestreo);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
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

	private long getPorcentajeMemoria(Long memoriaLibre, Long memoriaTotal) {
		long ml = memoriaLibre.longValue();
		long mt = memoriaTotal.longValue();
		return (mt-ml)*100/mt;
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
		if((!alarmasActivadas.isEmpty()) && (srv != null)) {
			srv.enviaListaAlarmas(alarmasActivadas);
		}
	}
}