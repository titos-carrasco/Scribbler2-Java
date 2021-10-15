package rcr.scribbler2.robot;

/**
 * Clase de apoyo para almacenar el estado de los sensores de luz del S2
 * <p>
 * El valor de cada sensor puede ser accesado directamente como <code>obj.lightLeft</code>,
 * <code>obj.lightCenter</code> y <code>obj.lightLeft</code>
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class HS2Lights {
    /** el sensor de luz izquierdo */
    public final int lightLeft;
    /** el sensor de luz central */
    public final int lightCenter;
    /** el sensor de luz derecho */
    public final int lightRight;

    /**
     * Construye un nuevo objeto para contener el valor de los sensores del luz del S2
     *
     * @param lightLeft el valor del sensor de luz izquierdo
     * @param lightCenter el valor del sensor de luz central
     * @param lightRight el valor del sensor de luz derecho
     */
    public HS2Lights( int lightLeft, int lightCenter , int lightRight ) {
        this.lightLeft = lightLeft;
        this.lightCenter = lightCenter;
        this.lightRight = lightRight;
    }

    /**
     * Retorna un objeto del tipo String representando el valor de los sensores de luz
     *
     *  @return un String representando la Coordenada
     */
    public String toString() {
        return "HS2Lights(" + lightLeft + ", " + lightCenter + ", " + lightRight + ")";
    }
}
