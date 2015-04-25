import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

public class Cliente {
    static public void main (String args[]) {
        if (args.length!=4) {
            System.err.println("Uso: Cliente hostregistro numPuertoRegistro SensorName tiempoMuestreo(ms)");
            return;
        }

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            ServicioAlarmas srv = (ServicioAlarmas) Naming.lookup("//" + args[0] + ":" + args[1] + "/RMISensor");
            SensorImpl s = new SensorImpl(Integer.parseInt(args[3]), srv, args[2]);
            srv.alta(s);
            System.out.println("Sensor activado, pulse enter para desactivar");
            Scanner input = new Scanner(System.in);
            input.nextLine();
            srv.baja(s);
            s.apagaMonitor();
            System.exit(0);
        }
        catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
        }
        catch (Exception e) {
            System.err.println("Excepcion en ClienteFabricaLog:");
            e.printStackTrace();
        }
    }
}
