import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by alberto on 21/4/15.
 */
public class MonitorTest {

    private static final int tiempoMuestreo = 2000;

    @Test
    public void testRun() throws Exception {
        Monitor m = new Monitor(tiempoMuestreo);
        Long value;
        m.start();
        for (int i = 0; i < 10; i++) {
            value = m.getCPU();
            if (value < 0 || value > 1) {
                fail("Valor no válido de CPU");
            } else {
                System.out.println("CPU = " + value.toString());
            }
        }
        m.stopThread();
    }
}