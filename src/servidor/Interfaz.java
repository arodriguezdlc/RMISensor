import java.util.List;
import java.util.Scanner;

/**
 * Created by alberto on 24/4/15.
 */
public class Interfaz {
    ServicioAlarmasImpl srv;

    Interfaz(ServicioAlarmasImpl srv) {
        this.srv = srv;
    }

    public void menu() {

        System.out.println("\nRMISENSOR\n");
        System.out.println("0) Regenerar menu");
        System.out.println("1) Ver sensores");
        System.out.println("2) Crear nueva Alarma");
        System.out.println("q) Apagar Servidor");

        System.out.println("\nSeleccione una opcion: ");

    }

    public void ejecutaOpcion() {
        Scanner input = new Scanner(System.in);
        String opcion = new String();
        while (!opcion.equals("q")) {
            opcion = input.next();
            switch (opcion) {
                case "0":
                    this.menu();
                    break;
                case "1":
                    this.printSensores();
                    break;
                case "2":
                    this.crearAlarma();
                    break;
                default:
                    System.out.println("Opcion no valida");
                    break;
            }
        }
    }

    private void printSensores() {
        List<Sensor> listaSensores = srv.getListaSensores();
        System.out.println("LISTA DE SENSORES: ");
        for (Sensor s : listaSensores) {
            int i = 0;
            System.out.println(Integer.valueOf(i).toString() + ")\t" + s.getSensorName());
            i++;
        }
    }

    private void crearAlarma() {
        Scanner input = new Scanner(System.in);
        boolean salir = true;
        Boolean esMayorQueUmbralBool = true;

        System.out.println("Introduzca el numero del sensor: ");
        String sensorNumber = input.next();
        Sensor s = srv.getListaSensores().get(Integer.parseInt(sensorNumber));

        System.out.print("Introduzca titulo alarma: ");
        String titulo = input.next();

        System.out.print("Introduzca descripcion alarma ");
        String descripcion = input.next();

        System.out.print("Introduzca prioridad alarma ");
        String prioridad = input.next();

        System.out.print("Introduzca parametro ");
        String parametro = input.next();

        System.out.print("Introduzca umbral");
        String umbral = input.next();
        do {
            System.out.print("¿Alarma si es mayor que umbral? (Y/N)");
            String esMayorQueUmbral = input.next();
            if (esMayorQueUmbral.equals("Y")) {
                esMayorQueUmbralBool = true;
            } else if (esMayorQueUmbral.equals("N")) {
                esMayorQueUmbralBool = false;
            } else {
                salir = false;
            }
        } while (!salir);

        s.crearAlarma(new Alarma(titulo, descripcion, parametro,
                Double.valueOf(umbral), esMayorQueUmbralBool, prioridad));

    }
}

