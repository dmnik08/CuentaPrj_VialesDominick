import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Programa principal con menu para gestionar cuentas.
 *
 * <p>Opciones requeridas:
 * 1) Crear Cuenta
 * 2) Conocer la cantidad de Cuentas Creadas
 * 3) Listar Cuentas
 * 4) Seleccionar Cuenta actual
 * 5) Depositar
 * 6) Retirar
 * 7) Consultar Saldo
 * 8) Consultar Estado (toString)
 * 0) Salir
 *
 * <p>Opcion adicional:
 * 9) Actualizar nombre del cuentahabiente
 */
public class PrincipalCuenta {

  private static final String MENU_PRINCIPAL =
      "\nMenu principal\n"
          + "1) Crear Cuenta\n"
          + "2) Conocer la cantidad de Cuentas Creadas\n"
          + "3) Listar Cuentas\n"
          + "4) Seleccionar Cuenta actual\n"
          + "5) Depositar\n"
          + "6) Retirar\n"
          + "7) Consultar Saldo\n"
          + "8) Consultar Estado (toString)\n"
          + "9) Actualizar nombre del cuentahabiente\n"
          + "0) Salir\n"
          + "Seleccione una opcion: ";

  private final List<Cuenta> cuentas;
  private Cuenta cuentaActual;

  public PrincipalCuenta() {
    this.cuentas = new ArrayList<>();
    this.cuentaActual = null;
  }

  public static void main(String[] args) {
    PrincipalCuenta app = new PrincipalCuenta();
    app.run();
  }

  private void run() {
    try (Scanner scanner = new Scanner(System.in)) {
      boolean seguir = true;
      while (seguir) {
        System.out.print(MENU_PRINCIPAL);
        String opcion = scanner.nextLine().trim();
        switch (opcion) {
          case "1":
            crearCuenta(scanner);
            break;
          case "2":
            conocerCantidadCuentasCreadas();
            break;
          case "3":
            listarCuentas();
            break;
          case "4":
            seleccionarCuentaActual(scanner);
            break;
          case "5":
            depositar(scanner);
            break;
          case "6":
            retirar(scanner);
            break;
          case "7":
            consultarSaldo();
            break;
          case "8":
            consultarEstado();
            break;
          case "9":
            actualizarNombre(scanner);
            break;
          case "0":
            System.out.println("Saliendo...");
            seguir = false;
            break;
          default:
            System.out.println("Opcion invalida.");
        }
      }
    }
  }

  // --------- Acciones del menu ---------

  private void crearCuenta(Scanner scanner) {
    System.out.println("\nCrear Cuenta");
    System.out.println("1) Constructor con saldo inicial");
    System.out.println("2) Constructor con nombre y saldo inicial");
    System.out.print("Seleccione una opcion: ");
    String tipo = scanner.nextLine().trim();

    try {
      switch (tipo) {
        case "1": {
          double saldo = leerDouble(scanner, "Ingrese saldo inicial (>=0): ");
          Cuenta c = new Cuenta(saldo);
          cuentas.add(c);
          System.out.println("Cuenta creada: " + c);
          System.out.println("Recuerde que puede establecer el nombre con la opcion 9 del menu.");
          break;
        }
        case "2": {
          System.out.print("Ingrese nombre del cuentahabiente: ");
          String nombre = scanner.nextLine();
          double saldo = leerDouble(scanner, "Ingrese saldo inicial (>=0): ");
          Cuenta c = new Cuenta(nombre, saldo);
          cuentas.add(c);
          System.out.println("Cuenta creada: " + c);
          break;
        }
        default:
          System.out.println("Opcion invalida.");
      }
    } catch (IllegalArgumentException ex) {
      System.out.println("No se pudo crear la cuenta: " + ex.getMessage());
    }
  }

  private void conocerCantidadCuentasCreadas() {
    System.out.println("Cantidad total de cuentas creadas: " + Cuenta.getCuentasCreadas());
  }

  private void listarCuentas() {
    if (cuentas.isEmpty()) {
      System.out.println("No hay cuentas registradas.");
      return;
    }
    System.out.println("\nListado de cuentas:");
    for (Cuenta c : cuentas) {
      String marcaActual = (cuentaActual != null && c.getAccountId() == cuentaActual.getAccountId())
          ? " [ACTUAL]" : "";
      System.out.println("- " + c + marcaActual);
    }
  }

  private void seleccionarCuentaActual(Scanner scanner) {
    if (cuentas.isEmpty()) {
      System.out.println("No hay cuentas para seleccionar.");
      return;
    }
    int id = leerEntero(scanner, "Ingrese el accountId de la cuenta a seleccionar: ");
    Cuenta encontrada = buscarPorId(id);
    if (encontrada == null) {
      System.out.println("No se encontro una cuenta con id " + id + ".");
    } else {
      cuentaActual = encontrada;
      System.out.println("Cuenta actual seleccionada: " + cuentaActual);
    }
  }

  private void depositar(Scanner scanner) {
    if (!hayCuentaActual()) {
      return;
    }
    double monto = leerDouble(scanner, "Ingrese el monto a depositar (>0): ");
    double saldoPosterior = cuentaActual.depositar(monto);
    System.out.println("Saldo posterior: " + saldoPosterior);
  }

  private void retirar(Scanner scanner) {
    if (!hayCuentaActual()) {
      return;
    }
    double monto = leerDouble(scanner, "Ingrese el monto a retirar (>0): ");
    double saldoAnterior = cuentaActual.getSaldo();
    double saldoPosterior = cuentaActual.retirar(monto);
    if (saldoPosterior == saldoAnterior && monto > 0 && monto > saldoAnterior) {
      System.out.println("Fondos insuficientes. Saldo sin cambios: " + saldoPosterior);
    } else {
      System.out.println("Saldo posterior: " + saldoPosterior);
    }
  }

  private void consultarSaldo() {
    if (!hayCuentaActual()) {
      return;
    }
    System.out.println("Saldo actual: " + cuentaActual.getSaldo());
  }

  private void consultarEstado() {
    if (!hayCuentaActual()) {
      return;
    }
    System.out.println(cuentaActual.toString());
  }

  private void actualizarNombre(Scanner scanner) {
    if (!hayCuentaActual()) {
      return;
    }
    System.out.print("Ingrese el nuevo nombre del cuentahabiente: ");
    String nombre = scanner.nextLine();
    try {
      cuentaActual.setAccountHolderName(nombre);
      System.out.println("Nombre actualizado. Estado: " + cuentaActual);
    } catch (IllegalArgumentException ex) {
      System.out.println("No se pudo actualizar el nombre: " + ex.getMessage());
    }
  }

  // --------- Utilidades ---------

  private boolean hayCuentaActual() {
    if (cuentaActual == null) {
      System.out.println("No hay cuenta actual seleccionada.");
      return false;
    }
    return true;
  }

  private Cuenta buscarPorId(int id) {
    for (Cuenta c : cuentas) {
      if (c.getAccountId() == id) {
        return c;
      }
    }
    return null;
  }

  private static double leerDouble(Scanner scanner, String prompt) {
    while (true) {
      System.out.print(prompt);
      String s = scanner.nextLine().trim();
      try {
        return Double.parseDouble(s);
      } catch (NumberFormatException ex) {
        System.out.println("Valor invalido. Intente de nuevo.");
      }
    }
  }

  private static int leerEntero(Scanner scanner, String prompt) {
    while (true) {
      System.out.print(prompt);
      String s = scanner.nextLine().trim();
      try {
        return Integer.parseInt(s);
      } catch (NumberFormatException ex) {
        System.out.println("Valor invalido. Intente de nuevo.");
      }
    }
  }
}
