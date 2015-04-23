package rcr.robots.scribbler2;

/**
 * Clase de apoyo para almacenar los sensores de línea del S2
 * <p>
 * Los sensores pueden ser accesados directamente como  <code>obj.lineLeft</code>
 * y <code>obj.lineRight</code>
 *
 * @see S2LineSensors
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class HS2LineSensors {
    /** el sensor de línea izquierdo */
    public final int lineLeft;
    /** el sensor de línea derecho */
    public final int lineRight;

    /**
     * Construye una nuevo objeto para almacenar los sensores de línea del S2
     *

     * @param lineLeft el sensor de línea izquierdo
     * @param lineRight el sensor de línea derecho
     */
    public HS2LineSensors( int lineLeft, int lineRight ) {
        this.lineLeft = lineLeft;
        this.lineRight = lineRight;
    }

    /**
     * Retorna un objeto del tipo String representando los sensores del S2
     *
     *  @return un String representando los sensores
     */
    public String toString() {
        return "HS2LineSensors(" + lineLeft + ", " + lineRight + ")";
    }
}
