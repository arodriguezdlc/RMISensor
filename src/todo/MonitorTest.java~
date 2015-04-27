import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;


/**
 * Created by alberto on 21/4/15.
 */
public class MonitorTest {

    private static final Integer tiempoMuestreo = 2000;

    @Test
    public void testRun() throws Exception {
        List<Alarma> l = new LinkedList<Alarma>();
        Monitor m = new Monitor(tiempoMuestreo, l);
        for (int i = 0; i < 10; i++) {
            double cpu = m.getCPU();
            if ( cpu < (double) 0 || cpu > (double) 100) {
                fail("Valor no válido de CPU");
            } else {
                System.out.println("CPU = " + Double.valueOf(cpu).toString());
            }

            System.out.println("RAM = " + Long.valueOf(m.getRam()).toString());
            try { Thread.sleep(1000); } catch(InterruptedException e) { }
        }
        m.stopThread();
    }
}