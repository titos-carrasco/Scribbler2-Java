package rcr.robots.scribbler2;

/**
 * Clase de apoyo para almacenar los sensores IR del S2
 * <p>
 * Los sensores pueden ser accesados directamente como  <code>obj.irLeft</code>
 * y <code>obj.irRight</code>
 *
 * @see S2IRSensors
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class HS2Infrared {
    /** el sensor IR izquierdo */
    public final int irLeft;
    /** el sensor IR derecho */
    public final int irRight;

    /**
     * Construye una nuevo objeto para almacenar los sensores IR del S2
     *

     * @param irLeft el sensor IR izquierdo
     * @param irRight el sensor IR derecho
     */
    public HS2Infrared( int irLeft, int irRight ) {
        this.irLeft = irLeft;
        this.irRight = irRight;
    }

    /**
     * Retorna un objeto del tipo String representando los sensores del S2
     *
     *  @return un String representando los sensores
     */
    public String toString() {
        return "HS2Infrared(" + irLeft + ", " + irRight + ")";
    }
}
