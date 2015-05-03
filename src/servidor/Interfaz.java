import java.lang.NumberFormatException;
import java.lang.System;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

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
        System.out.println("3) Ver alarmas");
        System.out.println("q) Apagar Servidor");

        System.out.println("\nSeleccione una opcion: ");

    }

    public void ejecutaOpcion() throws RemoteException {
        Scanner input = new Scanner(System.in);
        String opcion = new String();
        while (!opcion.equals("q")) {
            opcion = input.next();
            switch (opcion) {
                case "0":
                    System.out.print("\033[H\033[2J");
                    this.menu();
                    break;
                case "1":
                    System.out.print("\033[H\033[2J");
                    this.printSensores();
                    this.menu();
                    break;
                case "2":
                    System.out.print("\033[H\033[2J");
                    this.crearAlarma();
                    this.menu();
                    break;
                case "3":
                    System.out.print("\033[H\033[2J");
                    this.verAlarmas();
                    this.menu();
                    break;
                case "q":                    
                    System.out.println("Apagando servidor");
                    this.menu();
                    break;
                default:
                    System.out.print("\033[H\033[2J");
                    System.out.println("Opcion no valida");
                    this.menu();
                    break;
            }
        }
    }

    private void printSensores() throws RemoteException {
        List<Sensor> listaSensores = srv.getListaSensores();
        System.out.println("LISTA DE SENSORES: ");
        int i = 0;
        for (Sensor s : listaSensores) {
            System.out.println(Integer.valueOf(i).toString() + ")\t" + s.getSensorName());
            i++;
        }
    }

    private void crearAlarma() throws RemoteException {
        Scanner input = new Scanner(System.in);
        Boolean esMayorQueUmbralBool = true;
        Sensor s = null;

        do {
            this.printSensores();
            System.out.println("Introduzca el numero del sensor: ");
            String sensorNumber = input.nextLine();
            try {
                s = srv.getListaSensores().get(Integer.parseInt(sensorNumber));
            }catch (IndexOutOfBoundsException e){
                System.out.println("El indice especificado es inválido");
            }catch (NumberFormatException e){
                System.out.println("Debe introducir un número");
            }
        }while(s == null);

        System.out.print("Introduzca titulo alarma: ");
        String titulo = input.nextLine();

        System.out.print("Introduzca descripcion alarma: ");
        String descripcion = input.nextLine();

        String prioridad;
        do {
            System.out.print("Introduzca prioridad alarma [alta/media/baja]: ");
            prioridad = input.nextLine();
        } while (!prioridad.equals("alta") && !prioridad.equals("media") && !prioridad.equals("baja"));

        String parametro;
        do {
            System.out.print("Introduzca parametro[CPU/RAM]: ");
            parametro = input.nextLine();
        } while (!parametro.equals("CPU") && !parametro.equals("RAM"));
        
        String umbral = null;
        boolean thresholdValid = true;
        do {
            thresholdValid = true;
            System.out.print("Introduzca umbral: ");
            umbral = input.nextLine();

            try {
                if (Float.valueOf(umbral) < 0.0) {
                    System.out.println("El valor introducido debe ser mayor que 0.");
                    thresholdValid = false;
                }
            }catch (NumberFormatException e){
                System.out.println("El valor introducido no es un numero");
                thresholdValid = false;
            }
        } while(!thresholdValid);

        boolean salir = true;
        do {
            salir = true;
            System.out.print("¿Alarma si es mayor que umbral? (Y/N)");
            String esMayorQueUmbral = input.nextLine();
            if (esMayorQueUmbral.equals("Y")) {
                esMayorQueUmbralBool = true;
            } else if (esMayorQueUmbral.equals("N")) {
                esMayorQueUmbralBool = false;
            } else {
                salir = false;
            }
        } while (!salir);

        s.crearAlarma(new Alarma(titulo, descripcion, parametro,
                Double.valueOf(umbral), esMayorQueUmbralBool, prioridad, s));

    }
    private void verAlarmas() throws RemoteException {
        this.printSensores();
        System.out.println("Introduzca el numero de sensor:");
        Scanner input = new Scanner(System.in);
        String numSensor = input.next();
        Sensor s = srv.getListaSensores().get(Integer.parseInt(numSensor));
        List<Alarma> listaAlarmas = s.getListaAlarmas();
        System.out.println("Lista de alarmas del sensor " + s.getSensorName());
        for(Alarma a : listaAlarmas) {
            System.out.println(a.toString());
        }
    }
}

