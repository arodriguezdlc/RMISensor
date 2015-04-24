import java.util.*;
import java.rmi.*;
import java.rmi.server.*;

class Cliente {
    static public void main (String args[]) {
        if (args.length!=3) {
            System.err.println("Uso: Cliente hostregistro numPuertoRegistro tiempoMuestreo(ms)");
            return;
        }

        if (System.getSecurityManager() == null)
            System.setSecurityManager(new SecurityManager());

        try {
            ServicioAlarmas srv = (ServicioAlarmas) Naming.lookup("//" + args[0] + ":" + args[1] + "/RMISensor");
            Sensor s = new SensorImpl(Integer.parseInt(args[2]), srv);
            srv.alta(s);
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
