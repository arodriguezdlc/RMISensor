import java.util.*;

public class MonitorTest {

    private static final Integer tiempoMuestreo = 2000;

    
    public void testRun() throws Exception {
        List<Alarma> l = new LinkedList<Alarma>();
        Monitor m = new Monitor(tiempoMuestreo, l);
        for (int i = 0; i < 10; i++) {
            double cpu = m.getCPU();
            if ( cpu < (double) 0 || cpu > (double) 100) {
                System.out.println("Valor no válido de CPU");
            } else {
                System.out.println("CPU = " + Double.valueOf(cpu).toString());
            }

            System.out.println("RAM = " + Long.valueOf(m.getRam()).toString());
            try { Thread.sleep(1000); } catch(InterruptedException e) { }
        }
        m.stopThread();
    }
}
