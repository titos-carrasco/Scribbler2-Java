package rcr.robots.scribbler2;

/**
 * Clase de apoyo para almacenar el estado del S2 registrado en los pines de entrada y salida
 * <p>
 * Los pines pueden ser accesados directamente como <code>obj.inPins</code>
 * y <code>obj.outPins</code>
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class HS2State {
    /** estado registrado en los pines de entrada */
    public final int inPins;
    /** estado registrado en los pines de salida */
    public final int outPins;

    /**
     * Construye un nuevo objeto para almacenar el estado registrado en los pines del S2
     *
     * @param inPins el estado en los pines de entrada
     * @param outPins el estado en los pines de salida
     */
    public HS2State( int inPins, int outPins ) {
        this.inPins = inPins;
        this.outPins = outPins;
    }

    /**
     * Retorna un objeto del tipo String representando el estado del S2 registrado en los pines
     *
     *  @return un String representando los pines y su estado
     */
    public String toString() {
        return "HS2State(" + inPins + ", " + outPins + ")";
    }
}
