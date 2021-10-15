package rcr.scribbler2.robot;

/**
 * Clase de apoyo para almacenar los sensores del S2
 * <p>
 * Los sensores pueden ser accesados directamente como <code>obj.irLeft</code>,
 * <code>obj.irRight</code>, <code>obj.lightLeft</code>, <code>obj.lightCenter</code>,
 * <code>obj.lightRight</code>, <code>obj.lineLeft</code>, <code>obj.lineRight</code>, y <code>obj.stall</code>
 *
 * @see Scribbler2
 * @author Roberto Carrasco
 */
public class HS2Sensors {
    /** el sensor infrarojo izquierdo */
    public final int irLeft;
    /** el sensor infrarojo derecho */
    public final int irRight;
    /** el sensor de luz izquierdo */
    public final int lightLeft;
    /** el sensor de luz del centro */
    public final int lightCenter;
    /** el sensor de luz derecho */
    public final int lightRight;
    /** el sensor de línea izquierdo */
    public final int lineLeft;
    /** el sensor de línea derecho */
    public final int lineRight;
    /** el indicador de ruedas trabadas */
    public final int stall;

    /**
     * Construye una nuevo objeto para almacenar los sensores del S2
     *
     * @param irLeft el sensor infrarojo izquierdo
     * @param irRight el sensor infrarojo derecho
     * @param lightLeft el sensor de luz izquierdo
     * @param lightCenter el sensor de luz del centroy
     * @param lightRight el sensor de luz derecho
     * @param lineLeft el sensor de línea izquierdo
     * @param lineRight el sensor de línea derecho
     * @param stall el indicador de ruedas trabadas
     */
    public HS2Sensors( int irLeft, int irRight,
                       int lightLeft, int lightCenter, int lightRight,
                       int lineLeft, int lineRight, int stall ) {
        this.irLeft = irLeft;
        this.irRight = irRight;
        this.lightLeft = lightLeft;
        this.lightCenter = lightCenter;
        this.lightRight = lightRight;
        this.lineLeft = lineLeft;
        this.lineRight = lineRight;
        this.stall = stall;
    }

    /**
     * Retorna un objeto del tipo String representando los sensores del S2
     *
     *  @return un String representando los sensores
     */
    public String toString() {
        return "IR(" + irLeft + ", " + irRight + "), " +
               "Light(" + lightLeft + ", " + lightCenter + ", " + lightRight + "), " +
               "Line(" + lineLeft + ", " + lineRight + "), " +
               "Stall(" + stall + ")";
    }
}
