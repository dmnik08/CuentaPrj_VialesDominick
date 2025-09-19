import java.util.Objects;

/**
 * Representa una cuenta bancaria simple.
 *
 * <p>Reglas:
 * - El saldo no puede ser negativo al construir.
 * - depositar y retirar retornan el saldo posterior a la operacion.
 * - retirar no modifica el saldo si no hay fondos suficientes.
 * - Existe un contador de cuentas creadas compartido por todas las instancias.
 */
public class Cuenta {
  private static int cuentasCreadas = 0;     // atributo de clase: contador global
  private static int nextAccountId = 1;      // atributo de clase: generador simple de id

  private final int accountId;               // atributo de instancia
  private String accountHolderName;          // atributo de instancia
  private double balance;                    // atributo de instancia

  /**
   * Crea una cuenta con saldo inicial y sin nombre.
   *
   * <p>El nombre puede establecerse luego con {@link #setAccountHolderName(String)}.
   *
   * @param saldoInicial saldo inicial (>= 0)
   * @throws IllegalArgumentException si el saldoInicial es negativo
   */
  public Cuenta(double saldoInicial) {
    validateNonNegative(saldoInicial, "saldoInicial");
    this.accountId = nextAccountId++;
    this.balance = saldoInicial;
    this.accountHolderName = null; // se puede establecer posteriormente
    cuentasCreadas++;
  }

  /**
   * Crea una cuenta con nombre y saldo inicial.
   *
   * @param nombre nombre del cuentahabiente (no null ni vacio)
   * @param saldoInicial saldo inicial (>= 0)
   * @throws IllegalArgumentException si nombre invalido o saldoInicial negativo
   */
  public Cuenta(String nombre, double saldoInicial) {
    validateHolderName(nombre);
    validateNonNegative(saldoInicial, "saldoInicial");
    this.accountId = nextAccountId++;
    this.accountHolderName = nombre;
    this.balance = saldoInicial;
    cuentasCreadas++;
  }

  /**
   * Deposita un monto y retorna el saldo posterior al deposito.
   *
   * @param monto monto a depositar (debe ser > 0)
   * @return saldo posterior
   */
  public double depositar(double monto) {
    if (monto <= 0) {
      // No hace cambios si el monto no es positivo
      return balance;
    }
    balance += monto;
    return balance;
  }

  /**
   * Retira un monto y retorna el saldo posterior al retiro.
   * Si no hay fondos suficientes, no modifica el saldo.
   *
   * @param monto monto a retirar (debe ser > 0)
   * @return saldo posterior (o saldo sin cambio si fondos insuficientes)
   */
  public double retirar(double monto) {
    if (monto <= 0) {
      return balance;
    }
    if (monto > balance) {
      // Fondos insuficientes: no modifica el saldo
      return balance;
    }
    balance -= monto;
    return balance;
  }

  /**
   * Devuelve el saldo actual.
   */
  public double getSaldo() {
    return balance;
  }

  /**
   * Establece/actualiza el nombre del cuentahabiente.
   *
   * @param nombre nuevo nombre (no null ni vacio)
   * @throws IllegalArgumentException si el nombre es invalido
   */
  public void setAccountHolderName(String nombre) {
    validateHolderName(nombre);
    this.accountHolderName = nombre;
  }

  /**
   * Devuelve el nombre del cuentahabiente (puede ser null si no se ha establecido).
   */
  public String getAccountHolderName() {
    return accountHolderName;
  }

  /**
   * Devuelve el identificador unico de la cuenta.
   */
  public int getAccountId() {
    return accountId;
  }

  /**
   * Retorna la cantidad total de cuentas creadas.
   */
  public static int getCuentasCreadas() {
    return cuentasCreadas;
  }

  @Override
  public String toString() {
    // Debe devolver TODO el estado del objeto
    return "Cuenta{"
        + "accountId=" + accountId
        + ", accountHolderName=" + (accountHolderName == null ? "(sin nombre)" : accountHolderName)
        + ", balance=" + balance
        + '}';
  }

  // --------- Helpers de validacion (privados) ---------

  private static void validateNonNegative(double value, String fieldName) {
    if (value < 0) {
      throw new IllegalArgumentException(fieldName + " no puede ser negativo");
    }
  }

  private static void validateHolderName(String nombre) {
    if (Objects.isNull(nombre) || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("nombre no puede ser null ni vacio");
    }
  }
}
