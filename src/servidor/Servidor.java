import java.rmi.*;
import java.rmi.server.*;

public class Servidor  {
    static public void main (String args[]) {
       if (args.length!=1) {
            System.err.println("Uso: Servidor numPuertoRegistro");
            return;
        }
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }
        try {
            ServicioAlarmasImpl srv = new ServicioAlarmasImpl();
            Naming.rebind("rmi://localhost:" + args[0] + "/RMISensor", srv);
        }
        catch (RemoteException e) {
            System.err.println("Error de comunicacion: " + e.toString());
            System.exit(1);
        }
        catch (Exception e) {
            System.err.println("Excepcion en Servidor:");
            e.printStackTrace();
            System.exit(1);
        }
    }
}